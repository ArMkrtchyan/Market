package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.NewsAndEvents
import com.armboldmind.grandmarket.data.models.responsemodels.NewsAndEventsResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum
import javax.inject.Inject

class NewsAndEventsMapper @Inject constructor() : IMapper<NewsAndEventsResponseModel, NewsAndEvents> {
    override fun map(data: NewsAndEventsResponseModel): NewsAndEvents {
        return NewsAndEvents(
            id = data.id ?: 0L,
            title = data.title ?: "",
            description = data.description ?: "",
            newsCoverPhoto = data.newsCoverPhoto?.let { it + ImageSizesEnum.MEDIUM.size } ?: "",
            createdDate = data.createdDate ?: 0L,
        )
    }
}