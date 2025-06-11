package com.rymin.mirroring.model

data class MirroringSettings(
    val resolution: String,
    val fps: Int,
    val bitrate: String,
    val port: Int,
    val audioEnabled: Boolean
)

data class SystemInfo(
    val deviceIp: String,
    val wifiNetwork: String,
    val screenResolution: String,
    val androidVersion: String
)