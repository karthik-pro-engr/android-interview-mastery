package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

sealed interface StartupUiState {
    data object Loading : StartupUiState

    data object Authenticated : StartupUiState

    data object Unauthenticated : StartupUiState

    data class Error(
        val message: String
    ) : StartupUiState
}