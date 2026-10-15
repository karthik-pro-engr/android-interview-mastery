package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup.StartupScreen
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup.StartupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val startupViewModel: StartupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val uiState by startupViewModel.uiState.collectAsState()

            MaterialTheme {
                StartupScreen(
                    state = uiState,
                    onRetry = startupViewModel::retry
                )
            }
        }
    }
}