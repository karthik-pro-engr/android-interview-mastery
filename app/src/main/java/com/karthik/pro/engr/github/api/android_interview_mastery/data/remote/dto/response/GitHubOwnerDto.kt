package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubOwnerDto(
    val login: String,
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    @SerialName("html_url")
    val htmlUrl: String
)
