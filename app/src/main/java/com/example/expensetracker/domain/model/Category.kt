package com.example.expensetracker.domain.model

/**
 * Domain model for an expense category.
 */
data class Category(
    val id: Long = 0,
    val emoji: String,
    val name: String
) 