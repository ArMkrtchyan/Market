package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.responsemodels.CategoryResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface ICategoryService {
    @POST("category/getAllCategoriesByHierarchyForWeb")
    suspend fun getTopCategories(): Response<ResponseModel<List<CategoryResponseModel>>>
}