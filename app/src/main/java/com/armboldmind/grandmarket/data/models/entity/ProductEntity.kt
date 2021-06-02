package com.armboldmind.grandmarket.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0)
