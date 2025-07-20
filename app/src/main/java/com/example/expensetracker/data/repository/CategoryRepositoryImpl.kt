package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.mapper.CategoryMapper
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories().map { list -> list.map { CategoryMapper.toDomain(it) } }

    override suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)?.let { CategoryMapper.toDomain(it) }

    override suspend fun addCategory(category: Category): Long =
        categoryDao.insert(CategoryMapper.toEntity(category))

    override suspend fun updateCategory(category: Category) =
        categoryDao.update(CategoryMapper.toEntity(category))

    override suspend fun deleteCategory(category: Category) =
        categoryDao.delete(CategoryMapper.toEntity(category))
} 