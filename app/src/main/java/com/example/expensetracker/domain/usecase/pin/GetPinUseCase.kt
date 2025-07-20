package com.example.expensetracker.domain.usecase.pin

import com.example.expensetracker.domain.model.Pin
import com.example.expensetracker.domain.repository.PinRepository
import javax.inject.Inject

class GetPinUseCase @Inject constructor(
    private val repository: PinRepository
) {
    suspend operator fun invoke(): Pin? = repository.getPin()
} 