package com.armboldmind.grandmarket.data.models.domain

import com.armboldmind.gsport24.root.utils.DifItem

data class NewsAndEventsDetails(
    val id: Long,
    val title: String,
    val description: String,
    val mediaPreview: List<MediaPreviews>,
    val createdDate: Long,
) {
    data class MediaPreviews(val mediaUrl: String) : DifItem<MediaPreviews> {
        override fun areItemsTheSame(second: MediaPreviews): Boolean {
            return mediaUrl == second.mediaUrl
        }

        override fun areContentsTheSame(second: MediaPreviews): Boolean {
            return mediaUrl == second.mediaUrl
        }

    }
}