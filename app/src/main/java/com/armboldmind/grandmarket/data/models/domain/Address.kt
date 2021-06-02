package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val id: Long,
    var isDefaultForDelivery: Boolean,
    var additionalInformation: String?,
    var addressName: String,
    var apartment: String?,
    var entrance: Int?,
    var floor: Int?,
    var isDeliveryAddress: Boolean,
    var latitude: Double,
    var longitude: Double,
    var region: String,
    var title: String,
) : Parcelable, DifItem<Address> {
    override fun areItemsTheSame(second: Address): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Address): Boolean {
        return id == second.id && isDefaultForDelivery == second.isDefaultForDelivery && additionalInformation == second.additionalInformation && addressName == second.addressName && apartment == second.apartment && entrance == second.entrance && isDeliveryAddress == second.isDeliveryAddress && latitude == second.latitude && longitude == second.longitude && region == second.region && title == second.title
    }

}
