package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.entity.CategoryEntity
import com.example.expensetracker.domain.model.Category

object CategoryMapper {
    fun toDomain(entity: CategoryEntity): Category =
        Category(
            id = entity.id,
            emoji = entity.emoji,
            name = entity.name
        )

    fun toEntity(domain: Category): CategoryEntity =
        CategoryEntity(
            id = domain.id,
            emoji = domain.emoji,
            name = domain.name
        )
} 