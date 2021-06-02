package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.requestmodels.ContactUsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchRequestModel
import com.armboldmind.grandmarket.data.models.responsemodels.FAQResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.NewsAndEventsDetailsResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.NewsAndEventsResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.TermsAndPrivacyResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.*

interface IInfoService {

    @GET("settings/getTermsByEnumValue")
    suspend fun getTermsAndPrivacy(@Query("termEnumValue") type: Int): Response<ResponseModel<TermsAndPrivacyResponseModel>>

    @POST("faq/getAllFAQsForWeb")
    suspend fun getFAQ(@Body search: SearchRequestModel): Response<ResponseModel<List<FAQResponseModel>>>

    @POST("support/")
    suspend fun contactUs(@Body data: ContactUsRequestModel): Response<ResponseModel<Any>>

    @GET("news/getAllActiveNewsForMobile/{page}/{size}")
    suspend fun getNewsAndEvents(
        @Path("page") page: Int,
        @Path("size") size: Int,
        @Query("newsTypeValue") newsTypeValue: Int,
    ): Response<ResponseModel<PaginationResponseModel<NewsAndEventsResponseModel>>>

    @GET("news/getNewsById")
    suspend fun getNewsById(@Query("newsId") newsId: Long): Response<ResponseModel<NewsAndEventsDetailsResponseModel>>
}