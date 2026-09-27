package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueDto(
    val body: String,
    @SerialName("created_at")
    val createdAt: String,

    @SerialName("html_url")
    val htmlUrl: String,
    val id: Int,
    val number: Int,
    val state: String,
    val title: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val user: IssueUserDto
)