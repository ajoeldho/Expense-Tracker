package com.example.expensetracker.di

import com.example.expensetracker.data.repository.CategoryRepositoryImpl
import com.example.expensetracker.data.repository.ExpenseRepositoryImpl
import com.example.expensetracker.data.repository.PinRepositoryImpl
import com.example.expensetracker.domain.repository.CategoryRepository
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.PinRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        impl: ExpenseRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindPinRepository(
        impl: PinRepositoryImpl
    ): PinRepository
} 