package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.NewsAndEventsDetails
import com.armboldmind.grandmarket.data.models.responsemodels.NewsAndEventsDetailsResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum
import javax.inject.Inject

class NewsAndEventsDetailsMapper @Inject constructor() : IMapper<NewsAndEventsDetailsResponseModel, NewsAndEventsDetails> {
    override fun map(data: NewsAndEventsDetailsResponseModel): NewsAndEventsDetails {
        return NewsAndEventsDetails(
            id = data.id ?: 0L,
            title = data.title ?: "",
            description = data.description ?: "",
            mediaPreview = arrayListOf<NewsAndEventsDetails.MediaPreviews>().apply {
                data.mediaPreviewDtos?.let { list ->
                    list.map { item ->
                        this.add(NewsAndEventsDetails.MediaPreviews(item.mediaUrl?.let { it + ImageSizesEnum.MEDIUM.size } ?: ""))
                    }
                }
            },
            createdDate = data.createdDate ?: 0L,
        )
    }
}