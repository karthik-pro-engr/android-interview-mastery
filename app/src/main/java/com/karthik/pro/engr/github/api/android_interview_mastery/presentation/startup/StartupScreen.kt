package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun StartupScreen(
    state: StartupUiState,
    onLogin: (String, String) -> Unit,
    onRetry: () -> Unit,
    onTestRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state) {
                StartupUiState.Loading -> {
                    CircularProgressIndicator()
                    Text("Restoring token storage…")
                }

                is StartupUiState.Unauthenticated -> {
                    Text(
                        "DummyJSON Login",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    state.errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Button(
                        onClick = { onLogin(username, password) },
                        enabled = username.isNotBlank() &&
                                password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Sign in")
                    }
                }

                is StartupUiState.Working -> {
                    CircularProgressIndicator()
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is StartupUiState.Authenticated -> {
                    Text(
                        "Authenticated profile",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = state.profile.fullName,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text("Username: ${state.profile.username}")
                    Text("Email: ${state.profile.email}")
                    Text("User ID: ${state.profile.id}")

                    state.statusMessage?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Button(
                        onClick = onTestRefresh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Run refresh drill")
                    }

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Log out")
                    }
                }

                is StartupUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Retry")
                    }

                    Button(onClick = onLogout) {
                        Text("Clear session")
                    }
                }
            }
        }
    }
}