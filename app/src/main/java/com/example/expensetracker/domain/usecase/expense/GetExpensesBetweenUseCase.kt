package com.example.expensetracker.domain.usecase.expense

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetExpensesBetweenUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(start: LocalDateTime, end: LocalDateTime): Flow<List<Expense>> =
        repository.getExpensesBetween(start, end)
} 