package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    val topics:List<String>,
    val owner: GitHubOwnerDto
)
