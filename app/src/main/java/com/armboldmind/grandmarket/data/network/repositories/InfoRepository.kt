package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.IInfoRepository
import com.armboldmind.grandmarket.data.mappers.NewsAndEventsDetailsMapper
import com.armboldmind.grandmarket.data.mappers.NewsAndEventsMapper
import com.armboldmind.grandmarket.data.models.domain.NewsAndEvents
import com.armboldmind.grandmarket.data.models.domain.NewsAndEventsDetails
import com.armboldmind.grandmarket.data.models.requestmodels.ContactUsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchRequestModel
import com.armboldmind.grandmarket.data.models.responsemodels.FAQResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.TermsAndPrivacyResponseModel
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IInfoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InfoRepository @Inject constructor(
    private val mIInfoService: IInfoService,
    private val newsAndEventsMapper: NewsAndEventsMapper,
    private val newsAndEventsDetailsMapper: NewsAndEventsDetailsMapper,
    mApplication: Context,
) : BaseDataSource(mApplication), IInfoRepository {
    override suspend fun getTermsAndPrivacy(type: Int): Flow<TermsAndPrivacyResponseModel> {
        return getResult { mIInfoService.getTermsAndPrivacy(type) }
    }

    override suspend fun getFAQ(search: SearchRequestModel): Flow<List<FAQResponseModel>> {
        return getResult { mIInfoService.getFAQ(search) }
    }

    override suspend fun contactUs(data: ContactUsRequestModel): Flow<Any> {
        return getResult { mIInfoService.contactUs(data) }
    }

    override suspend fun getNewsAndEvents(): Flow<PagingData<NewsAndEvents>> {
        return getPagingResult(newsAndEventsMapper) { page, size -> mIInfoService.getNewsAndEvents(page, size, 1) }
    }

    override suspend fun getNewsById(newsId: Long): Flow<NewsAndEventsDetails> {
        return getResultWithMapper(newsAndEventsDetailsMapper) { mIInfoService.getNewsById(newsId) }
    }
}