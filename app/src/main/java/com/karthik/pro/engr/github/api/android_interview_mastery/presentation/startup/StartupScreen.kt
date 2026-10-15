package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartupScreen(
    state: StartupUiState,
    onRetry: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state) {
                StartupUiState.Loading -> {
                    CircularProgressIndicator()

                    Text(
                        text = "Restoring your session…",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                StartupUiState.Authenticated -> {
                    Text(
                        text = "Session restored",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Navigate to your authenticated home screen here."
                    )
                }

                StartupUiState.Unauthenticated -> {
                    Text(
                        text = "Please log in",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Navigate to your login screen here."
                    )
                }

                is StartupUiState.Error -> {
                    Text(
                        text = "Unable to restore session",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = state.message,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}