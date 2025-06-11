package com.rymin.mirroring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mirroring.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val resolution: String = "1920x1080",
    val availableResolutions: List<String> = listOf(
        "3840x2160 (4K)",
        "2560x1440 (QHD)",
        "1920x1080 (Full HD)",
        "1280x720 (HD)",
        "854x480 (SD)"
    ),
    val fps: Int = 30,
    val bitrate: String = "5 Mbps",
    val availableBitrates: List<String> = listOf(
        "10 Mbps (최고품질)",
        "5 Mbps (고품질)",
        "3 Mbps (중품질)",
        "1.5 Mbps (저품질)",
        "0.8 Mbps (절약모드)"
    ),
    val port: Int = 8080,
    val audioEnabled: Boolean = true,
    val deviceIp: String = "",
    val wifiNetwork: String = "",
    val screenResolution: String = "",
    val androidVersion: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadSystemInfo()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            _uiState.value = _uiState.value.copy(
                resolution = settings.resolution,
                fps = settings.fps,
                bitrate = settings.bitrate,
                port = settings.port,
                audioEnabled = settings.audioEnabled
            )
        }
    }

    private fun loadSystemInfo() {
        viewModelScope.launch {
            val systemInfo = settingsRepository.getSystemInfo()
            _uiState.value = _uiState.value.copy(
                deviceIp = systemInfo.deviceIp,
                wifiNetwork = systemInfo.wifiNetwork,
                screenResolution = systemInfo.screenResolution,
                androidVersion = systemInfo.androidVersion
            )
        }
    }

    fun updateResolution(resolution: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(resolution = resolution)
            settingsRepository.updateResolution(resolution)
        }
    }

    fun updateFps(fps: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(fps = fps)
            settingsRepository.updateFps(fps)
        }
    }

    fun updateBitrate(bitrate: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(bitrate = bitrate)
            settingsRepository.updateBitrate(bitrate)
        }
    }

    fun updatePort(port: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(port = port)
            settingsRepository.updatePort(port)
        }
    }

    fun updateAudioEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(audioEnabled = enabled)
            settingsRepository.updateAudioEnabled(enabled)
        }
    }
}