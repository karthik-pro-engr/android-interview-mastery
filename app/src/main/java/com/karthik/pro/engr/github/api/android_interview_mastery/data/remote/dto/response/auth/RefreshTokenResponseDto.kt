package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)