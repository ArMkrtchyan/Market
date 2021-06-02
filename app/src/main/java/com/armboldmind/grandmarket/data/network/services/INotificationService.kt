package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.NotificationResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface INotificationService {
    @GET("notification/getParticipantNotifications/{page}/{size}")
    suspend fun getNotifications(@Path("page") page: Int, @Path("size") size: Int): Response<ResponseModel<PaginationResponseModel<NotificationResponseModel>>>

    @GET("notification/getUnseenNotificationsCount")
    suspend fun getUnseenCount(): Response<ResponseModel<Int>>

    @GET("notification/getUserPushSubscribe")
    suspend fun getUserPushSubscribe(): Response<ResponseModel<List<Int>>>

    @PUT("notification/enablePushNotification")
    suspend fun enablePushNotification(@Query("notificationType") notificationType: Int, @Query("enable") enable: Boolean): Response<ResponseModel<Boolean>>

}