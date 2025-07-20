package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesBetween(start: LocalDateTime, end: LocalDateTime): Flow<List<Expense>>
    suspend fun getExpenseById(id: Long): Expense?
    suspend fun addExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
} 