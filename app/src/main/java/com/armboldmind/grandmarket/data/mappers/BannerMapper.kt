package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Banner
import com.armboldmind.grandmarket.data.models.responsemodels.BannerResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum

class BannerMapper : IMapper<BannerResponseModel, Banner> {
    override fun map(data: BannerResponseModel): Banner {
        return Banner(id = data.id ?: 0L,
                      bannerActionEnum = data.bannerActionEnum ?: 0,
                      bannerActionId = data.bannerActionId ?: 0,
                      bannerTitle = data.bannerTitle ?: "",
                      bannerDescription = data.bannerDescription ?: "",
                      link = data.link ?: "",
                      mobileMedia = Banner.MediaFiles(
                          mediaUrl = data.mobileMedia?.mediaUrl?.let { it + ImageSizesEnum.MEDIUM.size } ?: "",
                          mediaType = data.mobileMedia?.mediaType ?: 0,
                          logo = data.mobileMedia?.logo ?: false,
                      ))
    }
}