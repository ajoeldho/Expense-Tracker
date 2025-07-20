package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.entity.ExpenseEntity
import com.example.expensetracker.data.mapper.CategoryMapper
import com.example.expensetracker.data.mapper.ExpenseMapper
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) : ExpenseRepository {
    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses().combine(categoryDao.getAllCategories()) { expenses, categories ->
            val categoryMap = categories.associateBy { it.name }
            expenses.map { entity ->
                val category = categoryMap[entity.category]?.let { CategoryMapper.toDomain(it) }
                    ?: CategoryMapper.toDomain(categories.first())
                ExpenseMapper.toDomain(entity, category)
            }
        }

    override fun getExpensesBetween(start: LocalDateTime, end: LocalDateTime): Flow<List<Expense>> =
        expenseDao.getExpensesBetween(
            start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        ).combine(categoryDao.getAllCategories()) { expenses, categories ->
            val categoryMap = categories.associateBy { it.name }
            expenses.map { entity ->
                val category = categoryMap[entity.category]?.let { CategoryMapper.toDomain(it) }
                    ?: CategoryMapper.toDomain(categories.first())
                ExpenseMapper.toDomain(entity, category)
            }
        }

    override suspend fun getExpenseById(id: Long): Expense? {
        val entity = expenseDao.getExpenseById(id) ?: return null
        val categories = categoryDao.getAllCategories()
        val category = categories.firstOrNull { it.name == entity.category }?.let { CategoryMapper.toDomain(it) }
            ?: return null
        return ExpenseMapper.toDomain(entity, category)
    }

    override suspend fun addExpense(expense: Expense): Long =
        expenseDao.insert(ExpenseMapper.toEntity(expense))

    override suspend fun updateExpense(expense: Expense) =
        expenseDao.update(ExpenseMapper.toEntity(expense))

    override suspend fun deleteExpense(expense: Expense) =
        expenseDao.delete(ExpenseMapper.toEntity(expense))
} 