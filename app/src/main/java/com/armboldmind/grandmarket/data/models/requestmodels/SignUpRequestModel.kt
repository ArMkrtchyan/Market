package com.armboldmind.grandmarket.data.models.requestmodels

class SignUpRequestModel(
    var fullName: String? = null,
    var userName: String? = null,
    var dateOfBirth: Long? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null,
    var code: String? = null,
    var uid: String? = null,
    var userTypeEnumValue: Int = 1,
)