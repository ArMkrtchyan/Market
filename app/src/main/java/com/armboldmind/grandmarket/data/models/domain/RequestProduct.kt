package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestProduct(
    val id: Long,
    val productName: String,
    val description: String,
    val categoryName: String,
    val brandName: String,
    val createdDate: Long,
    val attachedPictures: List<AttachedPictures>,
) : Parcelable, DifItem<RequestProduct> {
    override fun areItemsTheSame(second: RequestProduct): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: RequestProduct): Boolean {
        return id == second.id && productName == second.productName && description == second.description && categoryName == second.categoryName && brandName == second.brandName && createdDate == second.createdDate
    }

    @Parcelize
    data class AttachedPictures(val mediaUrl: String, val mediaType: Int, val logo: Boolean) : Parcelable
}
