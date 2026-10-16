package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val expiresInMins: Int,
    val refreshToken: String
)