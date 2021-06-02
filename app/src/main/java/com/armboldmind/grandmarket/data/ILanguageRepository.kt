package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import kotlinx.coroutines.flow.Flow

interface ILanguageRepository {
    suspend fun changeLanguage(language: Language): Flow<Boolean>
    suspend fun getLanguages(): Flow<List<Language>>
    suspend fun getAllKeys(): Flow<Keys>
}