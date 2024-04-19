package com.dhimandasgupta.common.android

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.IllegalArgumentException

@Immutable
sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}

/**
 * Network utility to get current state of internet connection
 */
val Context.currentConnectivityState: ConnectionState
    get() {
        if (this is Activity) throw IllegalArgumentException("Context must be Application Context")

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectionState {
    val connected = connectivityManager.getNetworkCapabilities(
        connectivityManager.activeNetwork
    )?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
fun Context.observeConnectivityAsFlow() = callbackFlow {
    if (this is Activity) throw IllegalArgumentException("Context must be Application Context")

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = networkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun networkCallback(
    callback: (ConnectionState) -> Unit
): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}