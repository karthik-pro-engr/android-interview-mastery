package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.startup.TokenStoreStartupCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val startupCoordinator: TokenStoreStartupCoordinator,
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<StartupUiState>(StartupUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        initializeSession()
    }

    fun retry() {
        initializeSession()
    }

    private fun initializeSession() {
        viewModelScope.launch {
            _uiState.value = StartupUiState.Loading

            try {
                startupCoordinator.ensureInitialized()

                val hasAccessToken =
                    !tokenStore.getAccessToken().isNullOrBlank()

                _uiState.value = if (hasAccessToken) {
                    StartupUiState.Authenticated
                } else {
                    StartupUiState.Unauthenticated
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = StartupUiState.Error(
                    message = exception.message
                        ?: "Unable to restore the session."
                )
            }
        }
    }
}