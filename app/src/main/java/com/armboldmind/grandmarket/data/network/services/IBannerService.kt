package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.BannerResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IBannerService {
    @GET("banner/getAll/{page}/{size}")
    suspend fun getBanners(@Path("page") page: Int, @Path("size") size: Int): Response<ResponseModel<PaginationResponseModel<BannerResponseModel>>>
}