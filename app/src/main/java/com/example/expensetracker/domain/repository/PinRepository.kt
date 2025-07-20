package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Pin

interface PinRepository {
    suspend fun setPin(pin: Pin)
    suspend fun getPin(): Pin?
    suspend fun clearPin()
} 