package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val expiresInMins: Int,
    val password: String,
    val username: String
)