package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.usecase.category.GetAllCategoriesUseCase
import com.example.expensetracker.domain.usecase.expense.AddExpenseUseCase
import com.example.expensetracker.domain.usecase.expense.UpdateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

sealed class AddExpenseUiState {
    object Idle : AddExpenseUiState()
    object Loading : AddExpenseUiState()
    object Success : AddExpenseUiState()
    data class Error(val message: String) : AddExpenseUiState()
}

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<AddExpenseUiState>(AddExpenseUiState.Idle)
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        getAllCategoriesUseCase().onEach { _categories.value = it }
            .catch { _categories.value = emptyList() }
            .launchIn(viewModelScope)
    }

    fun addExpense(amount: Double, category: Category, note: String?, dateTime: LocalDateTime) {
        _uiState.value = AddExpenseUiState.Loading
        viewModelScope.launch {
            val result = addExpenseUseCase(
                Expense(amount = amount, category = category, note = note, dateTime = dateTime)
            )
            _uiState.value = if (result.isSuccess) AddExpenseUiState.Success else AddExpenseUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    fun updateExpense(expense: Expense) {
        _uiState.value = AddExpenseUiState.Loading
        viewModelScope.launch {
            val result = updateExpenseUseCase(expense)
            _uiState.value = if (result.isSuccess) AddExpenseUiState.Success else AddExpenseUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }
} 