package com.armboldmind.grandmarket.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val title: String?,
    val message: String?,
    val sendDate: Long?,
    val action: Int?,
    val seen: Boolean?,
    val pageModel: String?,
)