package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.entity.ExpenseEntity
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ExpenseMapper {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toDomain(entity: ExpenseEntity, category: Category): Expense =
        Expense(
            id = entity.id,
            amount = entity.amount,
            category = category,
            note = entity.note,
            dateTime = LocalDateTime.parse(entity.dateTime, formatter)
        )

    fun toEntity(domain: Expense): ExpenseEntity =
        ExpenseEntity(
            id = domain.id,
            amount = domain.amount,
            category = domain.category.name, // Store category name for simplicity
            note = domain.note,
            dateTime = domain.dateTime.format(formatter)
        )
} 