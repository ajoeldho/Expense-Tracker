package com.example.expensetracker.domain.usecase.category

import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Long> {
        if (category.name.isBlank()) return Result.failure(IllegalArgumentException("Category name required"))
        if (category.emoji.isBlank()) return Result.failure(IllegalArgumentException("Emoji required"))
        return try {
            Result.success(repository.addCategory(category))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 