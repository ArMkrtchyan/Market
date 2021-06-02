package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ILanguageService {
    @GET("key/getAllKeysForWeb")
    suspend fun getAllKeys(): Response<ResponseModel<Keys>>

    @GET("language/getAllLanguages")
    suspend fun getAllLanguages(): Response<ResponseModel<List<Language>>>

    @PUT("user/changeLanguage")
    suspend fun changeLanguage(@Query("languageId") languageId: Int): Response<ResponseModel<Boolean>>
}