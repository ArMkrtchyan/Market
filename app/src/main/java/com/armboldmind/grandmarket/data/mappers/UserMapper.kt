package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.responsemodels.UserResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum
import java.util.*
import javax.inject.Inject

class UserMapper @Inject constructor() : IMapper<UserResponseModel, User> {
    override fun map(data: UserResponseModel): User {
        return User(data.userId ?: 0L,
                    User.PhysicalUser(
                        data.physicalUser?.firstName ?: "",
                        data.physicalUser?.lastName ?: "",
                        data.physicalUser?.fullName ?: "",
                        data.physicalUser?.dateOfBirth ?: Date().time,
                        data.physicalUser?.genderValue ?: 0,
                    ),
                    data.phoneNumber ?: "",
                    data.secondPhoneNumber ?: "",
                    data.defaultDeliveryAddress ?: "",
                    data.email ?: "",
                    data.imageUrl?.let { it + ImageSizesEnum.MEDIUM.size } ?: "",
                    data.userStatusValue ?: 0,
                    data.userTypeValue ?: 0,
                    data.enablePushNotification ?: false,
                    data.enableEmailNotification ?: false)
    }
}