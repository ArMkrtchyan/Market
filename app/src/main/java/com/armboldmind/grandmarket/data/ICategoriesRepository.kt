package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.Category
import kotlinx.coroutines.flow.Flow

interface ICategoriesRepository {
    suspend fun getAllCategories(): Flow<List<Category>>
}