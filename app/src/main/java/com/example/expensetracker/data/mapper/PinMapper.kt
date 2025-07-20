package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.entity.PinEntity
import com.example.expensetracker.domain.model.Pin

object PinMapper {
    fun toDomain(entity: PinEntity): Pin =
        Pin(pinHash = entity.pinHash)

    fun toEntity(domain: Pin): PinEntity =
        PinEntity(pinHash = domain.pinHash)
} 