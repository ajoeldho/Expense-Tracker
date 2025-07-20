package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.usecase.analytics.GetExpenseSummaryUseCase
import com.example.expensetracker.domain.usecase.expense.GetAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val today: Double,
        val week: Double,
        val month: Double,
        val recentExpenses: List<Expense>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    getExpenseSummaryUseCase: GetExpenseSummaryUseCase,
    getAllExpensesUseCase: GetAllExpensesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getExpenseSummaryUseCase().onEach { summary ->
            getAllExpensesUseCase().onEach { expenses ->
                _uiState.value = HomeUiState.Success(
                    today = summary.today,
                    week = summary.week,
                    month = summary.month,
                    recentExpenses = expenses.take(5)
                )
            }.catch { e ->
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }.launchIn(viewModelScope)
        }.catch { e ->
            _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
        }.launchIn(viewModelScope)
    }
} 