package com.armboldmind.grandmarket.data.network

import androidx.annotation.Keep

@Keep
data class ResponseModel<T>(val success: Boolean? = null, val data: T? = null, val message: String? = null)