package com.example.expensetracker.domain.usecase.expense

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<Long> {
        if (expense.amount <= 0) return Result.failure(IllegalArgumentException("Amount must be positive"))
        if (expense.category.name.isBlank()) return Result.failure(IllegalArgumentException("Category required"))
        return try {
            Result.success(repository.addExpense(expense))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 