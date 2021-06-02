package com.armboldmind.grandmarket.data

import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.models.domain.Notification
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
    suspend fun getNotifications(): Flow<PagingData<Notification>>
    suspend fun getUnseenCount(): Flow<Int>
    suspend fun getUserPushSubscribe(): Flow<List<Int>>
    suspend fun enablePushNotification(notificationType: Int, enable: Boolean): Flow<Boolean>
}