package com.example.expensetracker.domain.usecase.pin

import com.example.expensetracker.domain.repository.PinRepository
import javax.inject.Inject

class ClearPinUseCase @Inject constructor(
    private val repository: PinRepository
) {
    suspend operator fun invoke() = repository.clearPin()
} 