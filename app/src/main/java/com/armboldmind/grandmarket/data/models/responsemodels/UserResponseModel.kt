package com.armboldmind.grandmarket.data.models.responsemodels

data class UserResponseModel(
    var userId: Long?,
    var physicalUser: PhysicalUser?,
    val phoneNumber: String?,
    val secondPhoneNumber: String?,
    val defaultDeliveryAddress: String?,
    val email: String?,
    val imageUrl: String?,
    val userStatusValue: Int?,
    val userTypeValue: Int?,
    val enablePushNotification: Boolean?,
    val enableEmailNotification: Boolean?,
) {
    data class PhysicalUser(val firstName: String?, val lastName: String?, val fullName: String?, val dateOfBirth: Long?, val genderValue: Int?)
}