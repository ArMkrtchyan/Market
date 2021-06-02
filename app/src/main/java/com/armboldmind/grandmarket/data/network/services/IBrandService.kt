package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.requestmodels.BrandsRequestModel
import com.armboldmind.grandmarket.data.models.responsemodels.BrandResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IBrandService {

    @POST("brand/getAllBrandsForWeb/{page}/{size}")
    suspend fun getBrands(@Path("page") page: Int, @Path("size") size: Int, @Body data: BrandsRequestModel): Response<ResponseModel<PaginationResponseModel<BrandResponseModel>>>
}