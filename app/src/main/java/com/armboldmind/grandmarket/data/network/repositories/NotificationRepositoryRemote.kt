package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.INotificationRepository
import com.armboldmind.grandmarket.data.mappers.NotificationMapper
import com.armboldmind.grandmarket.data.models.domain.Notification
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.INotificationService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryRemote @Inject constructor(
    private val mINotificationService: INotificationService,
    private val notificationMapper: NotificationMapper,
    mApplication: Context,
) : BaseDataSource(mApplication), INotificationRepository {
    override suspend fun getNotifications(): Flow<PagingData<Notification>> {
        return getPagingResult(notificationMapper) { page, size -> mINotificationService.getNotifications(page, size) }
    }

    override suspend fun getUnseenCount(): Flow<Int> {
        return getResult { mINotificationService.getUnseenCount() }
    }

    override suspend fun getUserPushSubscribe(): Flow<List<Int>> {
        return getResult { mINotificationService.getUserPushSubscribe() }
    }

    override suspend fun enablePushNotification(notificationType: Int, enable: Boolean): Flow<Boolean> {
        return getResult { mINotificationService.enablePushNotification(notificationType, enable) }
    }


}