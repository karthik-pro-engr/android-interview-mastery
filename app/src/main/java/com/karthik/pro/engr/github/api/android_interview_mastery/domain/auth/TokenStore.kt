package com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth

interface TokenStore {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(
        accessToken: String,
        refreshToken: String
    )
    fun clearTokens()
}