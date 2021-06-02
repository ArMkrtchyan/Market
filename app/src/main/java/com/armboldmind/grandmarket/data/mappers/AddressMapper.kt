package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.responsemodels.AddressResponseModel

class AddressMapper : IMapper<AddressResponseModel, Address> {
    override fun map(data: AddressResponseModel): Address {
        return Address(
            id = data.id ?: 0,
            isDefaultForDelivery = data.isDefaultForDelivery ?: false,
            additionalInformation = data.additionalInformation,
            addressName = data.addressName ?: "",
            apartment = data.apartment,
            entrance = data.entrance,
            floor = data.floor,
            isDeliveryAddress = data.isDeliveryAddress ?: false,
            latitude = data.latitude ?: 0.0,
            longitude = data.longitude ?: 0.0,
            region = data.region ?: "",
            title = data.title ?: "",
        )
    }
}