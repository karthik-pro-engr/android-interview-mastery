package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.CreateIssueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(private val createIssueUseCase: CreateIssueUseCase) :
    ViewModel() {

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