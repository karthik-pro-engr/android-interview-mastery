package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.UserProfile
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unauthorized
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.GetProfileUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.LoginUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.toUserMessage
import com.karthik.pro.engr.github.api.android_interview_mastery.startup.TokenStoreStartupCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val startupCoordinator: TokenStoreStartupCoordinator,
    private val tokenStore: TokenStore,
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<StartupUiState>(StartupUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        restoreSession()
    }

    fun retry() {
        restoreSession()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = StartupUiState.Working("Signing in…")

            try {
                startupCoordinator.ensureInitialized()

                when (
                    val result = loginUseCase(
                        username = username.trim(),
                        password = password
                    )
                ) {
                    is Success -> loadProfile()

                    is Failure -> {
                        val message = if (result.error == Unauthorized) {
                            "Username or password is incorrect."
                        } else {
                            result.error.toUserMessage()
                        }

                        _uiState.value =
                            StartupUiState.Unauthenticated(message)
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = StartupUiState.Error(
                    "Unable to complete login. Please try again."
                )
            }
        }
    }

    fun testRefreshFlow() {
        viewModelScope.launch {
            try {
                startupCoordinator.ensureInitialized()

                val refreshToken = tokenStore.getRefreshToken()
                    ?.takeIf { it.isNotBlank() }
                    ?: run {
                        _uiState.value = StartupUiState.Unauthenticated(
                            "Please sign in before running the refresh drill."
                        )
                        return@launch
                    }

                withContext(Dispatchers.IO) {
                    tokenStore.saveTokens(
                        TokenPair(
                            accessToken = "aim-invalid-access-token",
                            refreshToken = refreshToken
                        )
                    )
                }

                loadProfile(
                    statusMessage =
                        "Profile loaded after the refresh flow completed."
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = StartupUiState.Error(
                    "The refresh drill could not be completed."
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    tokenStore.clearTokens()
                }

                _uiState.value = StartupUiState.Unauthenticated()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = StartupUiState.Error(
                    "Unable to clear the session."
                )
            }
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            _uiState.value = StartupUiState.Loading

            try {
                startupCoordinator.ensureInitialized()

                if (tokenStore.getAccessToken().isNullOrBlank()) {
                    _uiState.value = StartupUiState.Unauthenticated()
                } else {
                    loadProfile()
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = StartupUiState.Error(
                    "Unable to restore the session. Check your connection and retry."
                )
            }
        }
    }

    private suspend fun loadProfile(
        statusMessage: String? = null
    ) {
        _uiState.value = StartupUiState.Working("Loading your profile…")

        when (val result = getProfileUseCase()) {
            is Success -> {
                _uiState.value = StartupUiState.Authenticated(
                    profile = result.data,
                    statusMessage = statusMessage
                )
            }

            is Failure -> {
                if (result.error == Unauthorized) {
                    // Refresh failed or the session is no longer valid.
                    withContext(Dispatchers.IO) {
                        tokenStore.clearTokens()
                    }

                    _uiState.value = StartupUiState.Unauthenticated(
                        "Your session expired. Please sign in again."
                    )
                } else {
                    _uiState.value = StartupUiState.Error(
                        result.error.toUserMessage()
                    )
                }
            }
        }
    }
}