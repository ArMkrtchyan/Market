package com.armboldmind.grandmarket.shared.managers

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {
    companion object {

        const val TAG = "NetworkCheck"
    }

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        cm.registerNetworkCallback(NetworkRequest.Builder()
                                       .addCapability(NET_CAPABILITY_INTERNET)
                                       .build(), networkCallback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            cm.getNetworkCapabilities(network)
                ?.hasCapability(NET_CAPABILITY_INTERNET)
                .apply {
                    if (this == true) {
                        CoroutineScope(Dispatchers.IO).launch {
                            ping().collect { validNetworks.add(network); checkValidNetworks() }
                        }
                    }
                }
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }

    suspend fun ping(): Flow<Boolean> {
        return flow {
            Socket().apply { connect(InetSocketAddress("8.8.8.8", 53), 1500);close() }
            emit(true)
        }.catch { emit(false) }
    }
}