package com.armboldmind.grandmarket.data.models.domain

import androidx.lifecycle.MutableLiveData
import com.armboldmind.gsport24.root.utils.DifItem

data class Product(
    val id: Long,
    val productId: Long,
    val productAttributeId: Long,
    val productClusteredPriceId: String,
    val productPrice: Double,
    val discountAmount: Double,
    val discount: Int,
    val productPriceFormatted: String,
    val discountAmountFormatted: String,
    val productName: String,
    val productShortDescription: String,
    val productCoverPhoto: String,
    val brandId: Long,
    val brandName: String,
    val labelPreviews: List<LabelPreview>,
    var favorite: Boolean,
    val favoriteLiveData: MutableLiveData<Boolean>,
) : DifItem<Product> {
    override fun areItemsTheSame(second: Product): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Product): Boolean {
        return id == second.id && productId == second.productId && productAttributeId == second.productAttributeId && productClusteredPriceId == second.productClusteredPriceId && productPrice == second.productPrice && discountAmount == second.discountAmount && productName == second.productName && productShortDescription == second.productShortDescription && productCoverPhoto == second.productCoverPhoto && favorite == second.favorite && brandName == second.brandName && brandId == second.brandId
    }

    data class LabelPreview(
        val id: Long,
        val name: String,
        val colour: String,
    )
}
