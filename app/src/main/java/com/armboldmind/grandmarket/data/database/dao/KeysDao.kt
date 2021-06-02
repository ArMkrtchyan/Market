package com.armboldmind.grandmarket.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armboldmind.grandmarket.data.models.entity.Keys

@Dao
interface KeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveKeys(keys: Keys)

    @Query("SELECT * from Keys")
    suspend fun findKeys(): List<Keys>
}