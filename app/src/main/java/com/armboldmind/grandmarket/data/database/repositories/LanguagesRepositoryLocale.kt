package com.armboldmind.grandmarket.data.database.repositories

import com.armboldmind.grandmarket.data.database.dao.KeysDao
import com.armboldmind.grandmarket.data.models.entity.Keys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LanguagesRepositoryLocale @Inject constructor(private val keysDao: KeysDao) {
    suspend fun insertKeys(keys: Keys) {
        keysDao.saveKeys(keys.apply { id = 1 })
    }

    suspend fun getKeys(): Flow<Keys> {
        return flow {
            keysDao.findKeys()
                .mapIndexed { index, keys ->
                    if (index == 0) emit(keys.apply { id = 1 })
                }
        }
    }
}