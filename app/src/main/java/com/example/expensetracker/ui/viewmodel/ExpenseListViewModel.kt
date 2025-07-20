package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.usecase.expense.GetAllExpensesUseCase
import com.example.expensetracker.domain.usecase.expense.GetExpensesBetweenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

sealed class ExpenseListUiState {
    object Loading : ExpenseListUiState()
    data class Success(val expenses: List<Expense>) : ExpenseListUiState()
    data class Error(val message: String) : ExpenseListUiState()
}

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getAllExpensesUseCase: GetAllExpensesUseCase,
    private val getExpensesBetweenUseCase: GetExpensesBetweenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ExpenseListUiState>(ExpenseListUiState.Loading)
    val uiState: StateFlow<ExpenseListUiState> = _uiState.asStateFlow()

    fun loadAllExpenses() {
        _uiState.value = ExpenseListUiState.Loading
        getAllExpensesUseCase().onEach {
            _uiState.value = ExpenseListUiState.Success(it)
        }.catch { e ->
            _uiState.value = ExpenseListUiState.Error(e.message ?: "Unknown error")
        }.launchIn(viewModelScope)
    }

    fun loadExpensesBetween(start: LocalDateTime, end: LocalDateTime) {
        _uiState.value = ExpenseListUiState.Loading
        getExpensesBetweenUseCase(start, end).onEach {
            _uiState.value = ExpenseListUiState.Success(it)
        }.catch { e ->
            _uiState.value = ExpenseListUiState.Error(e.message ?: "Unknown error")
        }.launchIn(viewModelScope)
    }
} 