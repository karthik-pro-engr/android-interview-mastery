package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.error

import kotlinx.serialization.Serializable

@Serializable
data class GitHubErrorDto(
    val message: String
)