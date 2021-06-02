package com.armboldmind.grandmarket.data.models.responsemodels

data class NewsAndEventsDetailsResponseModel(
    val id: Long?,
    val title: String?,
    val description: String?,
    val mediaPreviewDtos: List<MediaPreviews>?,
    val createdDate: Long?,
) {
    data class MediaPreviews(val mediaUrl: String?)
}