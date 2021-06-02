package com.armboldmind.grandmarket.data.models.domain

import com.armboldmind.gsport24.root.utils.DifItem

data class Banner(
    val id: Long,
    val bannerActionEnum: Int,
    val bannerActionId: Long,
    val bannerTitle: String,
    val bannerDescription: String,
    val link: String,
    val mobileMedia: MediaFiles,
) : DifItem<Banner> {
    override fun areItemsTheSame(second: Banner): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Banner): Boolean {
        return id == second.id && bannerActionEnum == second.bannerActionEnum && bannerActionId == second.bannerActionId && bannerTitle == second.bannerTitle && bannerDescription == second.bannerDescription && link == second.link && mobileMedia.mediaUrl == second.mobileMedia.mediaUrl
    }

    data class MediaFiles(
        val mediaUrl: String,
        val mediaType: Int,
        val logo: Boolean,
    )
}
