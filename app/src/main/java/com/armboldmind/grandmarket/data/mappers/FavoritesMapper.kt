package com.armboldmind.grandmarket.data.mappers

import androidx.lifecycle.MutableLiveData
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.responsemodels.ProductResponseModel
import com.armboldmind.grandmarket.shared.globalextensions.format

class FavoritesMapper : IMapper<ProductResponseModel, Product> {
    override fun map(data: ProductResponseModel): Product {
        return Product(id = data.id ?: 0L,
                       productId = data.productId ?: 0L,
                       productAttributeId = data.productAttributeId ?: 0L,
                       productClusteredPriceId = data.productClusteredPriceId ?: "",
                       productPrice = data.productPrice ?: 0.0,
                       discountAmount = data.discountAmount ?: 0.0,
                       discount = getDiscount(data.discountAmount ?: 0.0, (data.productPrice ?: 0.0)),
                       productPriceFormatted = (data.productPrice ?: 0.0).format(),
                       discountAmountFormatted = (data.discountAmount ?: 0.0).format(),
                       productName = data.productName ?: "",
                       productShortDescription = data.productShortDescription ?: "",
                       productCoverPhoto = data.productCoverPhoto ?: "",
                       brandId = data.brandId ?: 0L,
                       brandName = data.brandName ?: "",
                       labelPreviews = arrayListOf<Product.LabelPreview>().apply {
                           data.labelPreviews?.let {
                               it.map { preview ->
                                   add(Product.LabelPreview(
                                       id = preview.id ?: 0L,
                                       name = preview.name ?: "",
                                       colour = preview.colour ?: "",
                                   ))
                               }
                           }
                       },
                       favorite = true,
                       favoriteLiveData = MutableLiveData(data.favorite ?: false))
    }

    private fun getDiscount(newPrice: Double, oldPrice: Double): Int {
        return (100 - newPrice * 100 / oldPrice).toInt()
    }
}