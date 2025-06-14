package com.rymin.mirroring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mirroring.ui.navigation.MirroringNavigation
import com.example.mirroring.ui.theme.MirroringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MirroringTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MirroringNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}