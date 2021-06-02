package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.RequestProductResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IRequestProductService {
    @GET("request/getAllRequestsForUser/{page}/{size}")
    suspend fun getRequests(@Path("page") page: Int, @Path("size") size: Int): Response<ResponseModel<PaginationResponseModel<RequestProductResponseModel>>>

    @GET("request/{id}")
    suspend fun getRequestById(@Path("id") id: Long): Response<ResponseModel<RequestProductResponseModel>>

    @POST("request/")
    @Multipart
    suspend fun createRequestForProduct(
        @Query("contactName") contactName: String,
        @Query("contactInformation") contactInformation: String,
        @Query("productName") productName: String,
        @Query("description") description: String? = null,
        @Query("categoryId") categoryId: Long,
        @Query("brandId") brandId: Long,
        @Part files: List<MultipartBody.Part?>?,
    ): Response<ResponseModel<Long>>

    @POST("request/")
    suspend fun createRequestForProduct(
        @Query("contactName") contactName: String,
        @Query("contactInformation") contactInformation: String,
        @Query("productName") productName: String,
        @Query("description") description: String? = null,
        @Query("categoryId") categoryId: Long,
        @Query("brandId") brandId: Long,
    ): Response<ResponseModel<Long>>
}