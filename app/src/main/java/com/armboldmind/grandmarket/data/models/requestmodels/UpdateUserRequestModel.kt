package com.armboldmind.grandmarket.data.models.requestmodels

class UpdateUserRequestModel {
    var physicalUser: PhysicalUser? = null
    var phoneNumber: String? = null
    var email: String? = null

    data class PhysicalUser(val fullName: String, val dateOfBirth: Long)
}
