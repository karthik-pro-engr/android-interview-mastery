package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.CreateIssueUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.GetReposListUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.github.repos.GithubUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import okhttp3.Dispatcher

import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val createIssueUseCase: CreateIssueUseCase,
    private val getReposListUseCase: GetReposListUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) :
    ViewModel() {

    private var _uiState = MutableStateFlow<GithubUiState>(GithubUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    init {
        observerUserName()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observerUserName() {
        _username.debounce(500.milliseconds)
            .filter { it.length >= 3 }
            .distinctUntilChanged()
            .mapLatest { getReposListUseCase(it, "updated", "desc", "") }
            .onEach { result ->
                when (result) {
                    is Failure -> _uiState.value = GithubUiState.Failure("error")
                    is Success -> _uiState.value = GithubUiState.Success(repos = result.data)
                }
            }.launchIn(viewModelScope + dispatcher)
    }

    fun onUsernameChanged(value: String) {
        _username.value = value
    }

    fun createIssue() {
        viewModelScope.launch {
            createIssueUseCase(
                owner = "karthik-pro-engr", repo = "android-interview-mastery",
                CreateIssue(
                    title = "Issue",
                    body = "Body"
                )
            )
        }
    }

}