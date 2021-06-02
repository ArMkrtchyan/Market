package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import com.armboldmind.grandmarket.data.models.responsemodels.RequestProductResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum

class RequestProductMapper : IMapper<RequestProductResponseModel, RequestProduct> {
    override fun map(data: RequestProductResponseModel): RequestProduct {
        return RequestProduct(id = data.id ?: 0L,
                              productName = data.productName ?: "",
                              description = data.description ?: "",
                              brandName = data.brandName ?: "",
                              categoryName = data.categoryName ?: "",
                              createdDate = data.createdDate ?: 0L,
                              attachedPictures = arrayListOf<RequestProduct.AttachedPictures>().apply {
                                  data.attachedPictures?.let {
                                      it.map { item ->
                                          add(RequestProduct.AttachedPictures(item.mediaUrl?.let { url -> url + ImageSizesEnum.MEDIUM.size } ?: "",
                                                                              item.mediaType ?: 0,
                                                                              item.logo ?: false))
                                      }
                                  }
                              })
    }
}