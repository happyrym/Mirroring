package com.rymin.mirroring.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.example.mirroring.data.model.MirroringSettings
import com.example.mirroring.data.model.SystemInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("mirroring_settings", Context.MODE_PRIVATE)

    suspend fun getSettings(): MirroringSettings {
        return MirroringSettings(
            resolution = sharedPreferences.getString("resolution", "1920x1080") ?: "1920x1080",
            fps = sharedPreferences.getInt("fps", 30),
            bitrate = sharedPreferences.getString("bitrate", "5 Mbps") ?: "5 Mbps",
            port = sharedPreferences.getInt("port", 8080),
            audioEnabled = sharedPreferences.getBoolean("audio_enabled", true)
        )
    }

    suspend fun updateResolution(resolution: String) {
        sharedPreferences.edit().putString("resolution", resolution).apply()
    }

    suspend fun updateFps(fps: Int) {
        sharedPreferences.edit().putInt("fps", fps).apply()
    }

    suspend fun updateBitrate(bitrate: String) {
        sharedPreferences.edit().putString("bitrate", bitrate).apply()
    }

    suspend fun updatePort(port: Int) {
        sharedPreferences.edit().putInt("port", port).apply()
    }

    suspend fun updateAudioEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("audio_enabled", enabled).apply()
    }

    suspend fun getSystemInfo(): SystemInfo {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val wifiNetwork = wifiInfo.ssid?.replace("\"", "") ?: "연결 안됨"

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenResolution = "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}"

        val deviceIp = getDeviceIp()

        return SystemInfo(
            deviceIp = deviceIp,
            wifiNetwork = wifiNetwork,
            screenResolution = screenResolution,
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
        )
    }

    private fun getDeviceIp(): String {
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
                "IP 없음"
            }
        } catch (e: Exception) {
            "IP 획득 실패"
        }
    }
}