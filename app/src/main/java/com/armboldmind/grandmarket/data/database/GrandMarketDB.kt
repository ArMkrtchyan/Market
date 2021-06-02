package com.armboldmind.grandmarket.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.armboldmind.grandmarket.data.database.dao.KeysDao
import com.armboldmind.grandmarket.data.database.dao.MessageDao
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.entity.NotificationEntity
import com.armboldmind.grandmarket.data.models.entity.ProductEntity

@Database(entities = [ProductEntity::class, NotificationEntity::class, Keys::class], version = 3, exportSchema = true)
abstract class GrandMarketDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun keysDao(): KeysDao

}