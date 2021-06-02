package com.armboldmind.grandmarket.data

import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.models.domain.NewsAndEvents
import com.armboldmind.grandmarket.data.models.domain.NewsAndEventsDetails
import com.armboldmind.grandmarket.data.models.requestmodels.ContactUsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchRequestModel
import com.armboldmind.grandmarket.data.models.responsemodels.FAQResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.TermsAndPrivacyResponseModel
import kotlinx.coroutines.flow.Flow

interface IInfoRepository {
    suspend fun getTermsAndPrivacy(type: Int): Flow<TermsAndPrivacyResponseModel>
    suspend fun getFAQ(search: SearchRequestModel): Flow<List<FAQResponseModel>>
    suspend fun contactUs(data: ContactUsRequestModel): Flow<Any>
    suspend fun getNewsAndEvents(): Flow<PagingData<NewsAndEvents>>
    suspend fun getNewsById(newsId: Long): Flow<NewsAndEventsDetails>
}