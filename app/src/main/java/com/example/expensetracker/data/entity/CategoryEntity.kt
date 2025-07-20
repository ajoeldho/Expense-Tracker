package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing an expense category.
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val emoji: String,
    val name: String
) 