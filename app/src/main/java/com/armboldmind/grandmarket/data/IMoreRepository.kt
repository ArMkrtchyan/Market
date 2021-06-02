package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.More
import com.armboldmind.grandmarket.data.models.entity.Keys
import kotlinx.coroutines.flow.Flow

interface IMoreRepository {
    suspend fun createGlobalItems(): Flow<List<More>>
    suspend fun createUserItems(): Flow<List<More>>
    suspend fun getKeys(): Flow<Keys>
}