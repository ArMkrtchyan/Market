package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.responsemodels.AddressResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.*

interface IAddressService {
    @GET("address/getAddressList")
    suspend fun getAllAddresses(): Response<ResponseModel<List<AddressResponseModel>>>

    @POST("address/")
    suspend fun addAddress(@Body address: Address): Response<ResponseModel<Long>>

    @PUT("address/{id}")
    suspend fun updateAddress(@Path("id") id: Long, @Body address: Address): Response<ResponseModel<Boolean>>

    @DELETE("address/{id}")
    suspend fun deleteAddress(@Path("id") id: Long): Response<ResponseModel<Boolean>>
}