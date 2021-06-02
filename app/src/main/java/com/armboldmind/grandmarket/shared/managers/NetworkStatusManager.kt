package com.armboldmind.grandmarket.shared.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.Keep
import com.armboldmind.grandmarket.shared.globalextensions.applicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.net.Socket

@Keep
object NetworkStatusManager {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT < 23) {
            connectivityManager.activeNetworkInfo?.let { networkInfo ->
                return networkInfo.isConnectedOrConnecting && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            }
        } else {
            connectivityManager.activeNetwork?.let { activeNetwork ->
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
        return false
    }

    suspend fun ping(): Flow<Boolean> {
        return flow {
            Socket().apply { connect(InetSocketAddress("8.8.8.8", 53), 1500);close() }
            emit(true)
        }.catch { emit(false) }
    }
}