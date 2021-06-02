package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.ILanguageRepository
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.ILanguageService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguagesRepositoryRemote @Inject constructor(
    private val mILanguageService: ILanguageService,
    mApplication: Context,
) : BaseDataSource(mApplication), ILanguageRepository {
    override suspend fun changeLanguage(language: Language): Flow<Boolean> {
        return getResult { mILanguageService.changeLanguage(language.id) }
    }

    override suspend fun getLanguages(): Flow<List<Language>> {
        return getResult { mILanguageService.getAllLanguages() }
    }

    override suspend fun getAllKeys(): Flow<Keys> {
        return getResult { mILanguageService.getAllKeys() }
    }
}