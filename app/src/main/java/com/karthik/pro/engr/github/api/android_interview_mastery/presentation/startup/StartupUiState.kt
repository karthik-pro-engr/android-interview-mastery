package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.UserProfile

sealed interface StartupUiState {

    data object Loading : StartupUiState

    data class Unauthenticated(
        val errorMessage: String? = null
    ) : StartupUiState

    data class Working(
        val message: String
    ) : StartupUiState

    data class Authenticated(
        val profile: UserProfile,
        val statusMessage: String? = null
    ) : StartupUiState

    data class Error(
        val message: String
    ) : StartupUiState
}