package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.PinDao
import com.example.expensetracker.data.mapper.PinMapper
import com.example.expensetracker.domain.model.Pin
import com.example.expensetracker.domain.repository.PinRepository
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(
    private val pinDao: PinDao
) : PinRepository {
    override suspend fun setPin(pin: Pin) = pinDao.setPin(PinMapper.toEntity(pin))
    override suspend fun getPin(): Pin? = pinDao.getPin()?.let { PinMapper.toDomain(it) }
    override suspend fun clearPin() = pinDao.clearPin()
} 