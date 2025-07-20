package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing expense records.
 */
@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY dateTime DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Query("SELECT * FROM expenses WHERE dateTime BETWEEN :start AND :end ORDER BY dateTime DESC")
    fun getExpensesBetween(start: String, end: String): Flow<List<ExpenseEntity>>
} 