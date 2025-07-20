package com.example.expensetracker.domain.usecase.analytics

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class ExpenseSummary(
    val today: Double,
    val week: Double,
    val month: Double
)

class GetExpenseSummaryUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<ExpenseSummary> = repository.getAllExpenses().map { expenses ->
        val now = LocalDate.now()
        val todayStart = now.atStartOfDay()
        val weekStart = now.with(java.time.DayOfWeek.MONDAY).atStartOfDay()
        val monthStart = now.withDayOfMonth(1).atStartOfDay()
        ExpenseSummary(
            today = expenses.filter { it.dateTime >= todayStart }.sumOf { it.amount },
            week = expenses.filter { it.dateTime >= weekStart }.sumOf { it.amount },
            month = expenses.filter { it.dateTime >= monthStart }.sumOf { it.amount }
        )
    }
} 