package com.armboldmind.grandmarket.di.modules

import android.content.Context
import androidx.room.Room
import com.armboldmind.grandmarket.data.database.GrandMarketDB
import com.armboldmind.grandmarket.data.database.Migrations
import com.armboldmind.grandmarket.data.database.dao.KeysDao
import com.armboldmind.grandmarket.data.database.dao.MessageDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): GrandMarketDB {
        return Room.databaseBuilder(context, GrandMarketDB::class.java, "grand_market_database")
                .addMigrations(Migrations.MIGRATION_1_2,Migrations.MIGRATION_2_3)
                .build()

    }

    @Singleton
    @Provides
    fun provideMessageDao(grandMarketDB: GrandMarketDB): MessageDao {
        return grandMarketDB.messageDao()
    }

    @Singleton
    @Provides
    fun provideKeysDao(grandMarketDB: GrandMarketDB): KeysDao {
        return grandMarketDB.keysDao()
    }
}
