package com.example.expensetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.dao.PinDao
import com.example.expensetracker.data.entity.CategoryEntity
import com.example.expensetracker.data.entity.ExpenseEntity
import com.example.expensetracker.data.entity.PinEntity

/**
 * Room database for the Expense Tracker app.
 */
@Database(
    entities = [ExpenseEntity::class, CategoryEntity::class, PinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun pinDao(): PinDao
} 