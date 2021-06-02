package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Brand
import com.armboldmind.grandmarket.data.models.responsemodels.BrandResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum

class BrandMapper : IMapper<BrandResponseModel, Brand> {
    override fun map(data: BrandResponseModel): Brand {
        return Brand(id = data.id ?: 0L, title = data.title ?: "", logo = data.logo?.let { it + ImageSizesEnum.SMALL.size } ?: "")
    }
}