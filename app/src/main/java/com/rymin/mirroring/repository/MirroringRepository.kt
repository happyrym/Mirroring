package com.rymin.mirroring.repository

import android.content.Context
import android.net.wifi.WifiManager
import com.example.mirroring.data.service.MirroringService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MirroringRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mirroringService: MirroringService
) {

    suspend fun getDeviceIp(): String {
        return try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress

            if (ipAddress != 0) {
                String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
            } else {
                // WiFi가 연결되지 않은 경우 다른 방법으로 IP 획득
                getLocalIpAddress()
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun getLocalIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is InetAddress) {
                        val hostAddress = address.hostAddress
                        if (hostAddress?.indexOf(':') == -1) { // IPv4
                            return hostAddress ?: ""
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    suspend fun startMirroring(): Boolean {
        return try {
            mirroringService.startMirroring()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun stopMirroring() {
        try {
            mirroringService.stopMirroring()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}