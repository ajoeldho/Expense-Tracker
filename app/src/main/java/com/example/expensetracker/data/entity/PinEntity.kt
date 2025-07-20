package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing the user's PIN (hashed).
 */
@Entity(tableName = "pin")
data class PinEntity(
    @PrimaryKey val id: Int = 0, // Only one PIN stored
    val pinHash: String
) 