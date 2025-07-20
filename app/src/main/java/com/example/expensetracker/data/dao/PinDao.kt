package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.PinEntity

/**
 * DAO for managing the user's PIN.
 */
@Dao
interface PinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPin(pin: PinEntity)

    @Query("SELECT * FROM pin WHERE id = 0")
    suspend fun getPin(): PinEntity?

    @Query("DELETE FROM pin")
    suspend fun clearPin()
} 