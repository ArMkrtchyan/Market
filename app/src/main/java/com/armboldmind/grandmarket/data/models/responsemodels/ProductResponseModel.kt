package com.armboldmind.grandmarket.data.models.responsemodels

data class ProductResponseModel(
    val id: Long?,
    val productId: Long?,
    val productAttributeId: Long?,
    val productClusteredPriceId: String?,
    val productPrice: Double?,
    val discountAmount: Double?,
    val productName: String?,
    val productShortDescription: String?,
    val productCoverPhoto: String?,
    val brandId: Long?,
    val brandName: String?,
    val labelPreviews: List<LabelPreview>?,
    val favorite: Boolean?,
) {
    data class LabelPreview(
        val id: Long?,
        val name: String?,
        val colour: String?,
    )
}