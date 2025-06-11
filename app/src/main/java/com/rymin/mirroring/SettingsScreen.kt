package com.rymin.mirroring

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 미러링 스펙 설정
            item {
                SettingsCard(title = "미러링 설정") {
                    Column {
                        // 해상도 설정
                        SettingItem(
                            title = "해상도",
                            description = "미러링 화질을 선택하세요"
                        ) {
                            var expanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = uiState.resolution,
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    uiState.availableResolutions.forEach { resolution ->
                                        DropdownMenuItem(
                                            text = { Text(resolution) },
                                            onClick = {
                                                viewModel.updateResolution(resolution)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Divider()

                        // FPS 설정
                        SettingItem(
                            title = "프레임율 (FPS)",
                            description = "초당 프레임 수: ${uiState.fps}"
                        ) {
                            Slider(
                                value = uiState.fps.toFloat(),
                                onValueChange = { viewModel.updateFps(it.toInt()) },
                                valueRange = 15f..60f,
                                steps = 8
                            )
                        }

                        Divider()

                        // 비트레이트 설정
                        SettingItem(
                            title = "비트레이트",
                            description = "영상 품질을 조절합니다"
                        ) {
                            var expanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = uiState.bitrate,
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    uiState.availableBitrates.forEach { bitrate ->
                                        DropdownMenuItem(
                                            text = { Text(bitrate) },
                                            onClick = {
                                                viewModel.updateBitrate(bitrate)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 네트워크 설정
            item {
                SettingsCard(title = "네트워크 설정") {
                    Column {
                        SettingItem(
                            title = "포트 번호",
                            description = "미러링 서버 포트"
                        ) {
                            OutlinedTextField(
                                value = uiState.port.toString(),
                                onValueChange = {
                                    it.toIntOrNull()?.let { port ->
                                        viewModel.updatePort(port)
                                    }
                                },
                                singleLine = true
                            )
                        }

                        Divider()

                        SettingItem(
                            title = "오디오 미러링",
                            description = "화면과 함께 오디오도 전송합니다"
                        ) {
                            Switch(
                                checked = uiState.audioEnabled,
                                onCheckedChange = { viewModel.updateAudioEnabled(it) }
                            )
                        }
                    }
                }
            }

            // 시스템 정보
            item {
                SettingsCard(title = "시스템 정보") {
                    Column {
                        InfoItem("디바이스 IP", uiState.deviceIp)
                        Divider()
                        InfoItem("WiFi 네트워크", uiState.wifiNetwork)
                        Divider()
                        InfoItem("화면 해상도", uiState.screenResolution)
                        Divider()
                        InfoItem("Android 버전", uiState.androidVersion)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun InfoItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}