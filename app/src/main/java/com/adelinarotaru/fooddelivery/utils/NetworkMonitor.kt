package com.adelinarotaru.fooddelivery.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMonitor(private val context: Context) {

    private val _networkFlow = MutableStateFlow(false)
    val networkFlow = _networkFlow.asStateFlow()

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateNetworkStatus(true)
        }

        override fun onLost(network: Network) {
            updateNetworkStatus(false)
        }
    }

    fun startMonitoring() {
        val networkRequest =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun updateNetworkStatus(isNetworkAvailable: Boolean) {
        _networkFlow.tryEmit(isNetworkAvailable)
    }
}


