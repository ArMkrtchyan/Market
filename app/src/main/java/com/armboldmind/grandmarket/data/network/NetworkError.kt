package com.armboldmind.grandmarket.data.network

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.SocketTimeoutException

object NetworkError {
    fun isServerError(error: Throwable): Boolean {
        return error is SocketTimeoutException
    }

    fun isNetworkError(error: Throwable): Boolean {
        return error is IOException
    }

    fun isAuthFailure(error: Throwable): Boolean {
        return if (error is HttpException) {
            error.code() == HTTP_UNAUTHORIZED || error.code() == HTTP_FORBIDDEN
        } else false
    }
}