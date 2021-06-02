package com.armboldmind.grandmarket.data.models.responsemodels

data class NotificationResponseModel(
    val id: Long?,
    val title: String?,
    val message: String?,
    val sendDate: Long?,
    var actionEnum: Int?,
    val seen: Boolean?,
    val actionId: Long?,
)
