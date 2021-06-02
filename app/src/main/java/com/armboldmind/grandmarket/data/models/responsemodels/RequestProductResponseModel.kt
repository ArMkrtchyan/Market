package com.armboldmind.grandmarket.data.models.responsemodels

data class RequestProductResponseModel(
    val id: Long?,
    val productName: String?,
    val description: String?,
    val categoryName: String?,
    val brandName: String?,
    val createdDate: Long?,
    val attachedPictures: List<AttachedPictures>?,
) {
    data class AttachedPictures(val mediaUrl: String?, val mediaType: Int?, val logo: Boolean?)
}
