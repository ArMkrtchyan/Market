package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Notification
import com.armboldmind.grandmarket.data.models.responsemodels.NotificationResponseModel
import com.armboldmind.grandmarket.shared.enums.NotificationActionEnum

class NotificationMapper : IMapper<NotificationResponseModel, Notification> {
    override fun map(data: NotificationResponseModel): Notification {
        return Notification(id = data.id ?: 0,
                            title = data.title ?: "",
                            message = data.message ?: "",
                            sendDate = data.sendDate ?: 0,
                            action = data.actionEnum ?: 0,
                            seen = data.seen ?: true,
                            icon = getEnum(data.actionEnum ?: 0).icon,
                            background = getEnum(data.actionEnum ?: 0).background,
                            actionId = data.actionId ?: 0L)

    }

    private fun getEnum(action: Int) = NotificationActionEnum.values()
            .find { it.action == action } ?: NotificationActionEnum.GLOBAL
}