package com.rymin.mirroring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mirroring.data.repository.MirroringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isConnected: Boolean = false,
    val isMirroring: Boolean = false,
    val deviceIp: String = "",
    val currentPort: Int = 8080
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mirroringRepository: MirroringRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        checkConnectionStatus()
    }

    private fun checkConnectionStatus() {
        viewModelScope.launch {
            val deviceIp = mirroringRepository.getDeviceIp()
            _uiState.value = _uiState.value.copy(
                isConnected = deviceIp.isNotEmpty(),
                deviceIp = deviceIp
            )
        }
    }

    fun startMirroring() {
        viewModelScope.launch {
            val result = mirroringRepository.startMirroring()
            if (result) {
                _uiState.value = _uiState.value.copy(isMirroring = true)
            }
        }
    }

    fun stopMirroring() {
        viewModelScope.launch {
            mirroringRepository.stopMirroring()
            _uiState.value = _uiState.value.copy(isMirroring = false)
        }
    }
}