package com.armboldmind.grandmarket.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    @JvmStatic val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Keys ADD COLUMN subscribe TEXT NOT NULL DEFAULT 'subscribe'")
            database.execSQL("ALTER TABLE Keys ADD COLUMN add_to_basket TEXT NOT NULL DEFAULT 'add_to_basket'")
        }
    }
    @JvmStatic val MIGRATION_2_3 = object : Migration(2,3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Keys ADD COLUMN choose TEXT NOT NULL DEFAULT 'choose'")
        }
    }
}