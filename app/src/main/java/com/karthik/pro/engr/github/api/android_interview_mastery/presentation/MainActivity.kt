package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup.StartupScreen
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup.StartupViewModel
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.ui.theme.AndroidinterviewmasteryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val startupViewModel: StartupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val state by startupViewModel.uiState.collectAsState()

            AndroidinterviewmasteryTheme {
                StartupScreen(
                    state = state,
                    onLogin = startupViewModel::login,
                    onRetry = startupViewModel::retry,
                    onTestRefresh = startupViewModel::testRefreshFlow,
                    onLogout = startupViewModel::logout
                )
            }
        }
    }
}