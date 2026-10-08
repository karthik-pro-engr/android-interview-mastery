package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.github.repos

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo

sealed interface GithubUiState {
    object Loading: GithubUiState
    data class Success(val repos: List<Repo>): GithubUiState
    data class Failure(val messge: String): GithubUiState
}