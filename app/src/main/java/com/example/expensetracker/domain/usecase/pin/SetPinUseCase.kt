package com.example.expensetracker.domain.usecase.pin

import com.example.expensetracker.domain.model.Pin
import com.example.expensetracker.domain.repository.PinRepository
import javax.inject.Inject

class SetPinUseCase @Inject constructor(
    private val repository: PinRepository
) {
    suspend operator fun invoke(pin: Pin): Result<Unit> {
        if (pin.pinHash.isBlank()) return Result.failure(IllegalArgumentException("PIN required"))
        return try {
            repository.setPin(pin)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 