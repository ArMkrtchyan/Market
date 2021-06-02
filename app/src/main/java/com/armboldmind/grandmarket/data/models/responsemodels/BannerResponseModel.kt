package com.armboldmind.grandmarket.data.models.responsemodels

data class BannerResponseModel(
    val id: Long?,
    val bannerActionEnum: Int?,
    val bannerActionId: Long?,
    val bannerTitle: String?,
    val bannerDescription: String?,
    val link: String?,
    val mobileMedia: MediaFiles?,
) {
    data class MediaFiles(
        val mediaUrl: String?,
        val mediaType: Int?,
        val logo: Boolean?,
    )
}
