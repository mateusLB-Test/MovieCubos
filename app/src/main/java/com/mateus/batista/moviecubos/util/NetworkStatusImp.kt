package com.mateus.batista.moviecubos.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import com.mateus.batista.data.util.NetworkStatus

class NetworkStatusImp(private val context: Context) : NetworkStatus {
    override fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }
}