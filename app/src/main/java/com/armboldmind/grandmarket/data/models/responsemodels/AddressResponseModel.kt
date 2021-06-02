package com.armboldmind.grandmarket.data.models.responsemodels

data class AddressResponseModel(
    val id: Long?,
    var isDefaultForDelivery: Boolean?,
    var additionalInformation: String? = null,
    var addressName: String? = null,
    var apartment: String? = null,
    var entrance: Int? = null,
    var floor: Int? = null,
    var isDeliveryAddress: Boolean? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var region: String? = null,
    var title: String? = null,
)
