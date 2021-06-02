package com.armboldmind.grandmarket.data.models.domain

data class User(
    var userId: Long,
    var physicalUser: PhysicalUser,
    var phoneNumber: String,
    val secondPhoneNumber: String,
    val defaultDeliveryAddress: String,
    var email: String,
    var imageUrl: String,
    val userStatusValue: Int,
    val userTypeValue: Int,
    val enablePushNotification: Boolean,
    val enableEmailNotification: Boolean,
) {
    data class PhysicalUser(val firstName: String, val lastName: String, var fullName: String, var dateOfBirth: Long, val genderValue: Int)
}
