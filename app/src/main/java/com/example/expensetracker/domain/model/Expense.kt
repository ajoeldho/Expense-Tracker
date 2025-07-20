package com.example.expensetracker.domain.model

import java.time.LocalDateTime

/**
 * Domain model for an expense.
 */
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val category: Category,
    val note: String?,
    val dateTime: LocalDateTime
) 