package com.armboldmind.grandmarket.data.models.requestmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendCodeRequestModel(
    var phoneNumber: String? = null,
    var email: String? = null,
    val userName: String? = null,
    val fullName: String? = null,
    val dateOfBirth: Long? = null,
    var code: String? = null,
    var uid: String? = null,
) : Parcelable
