package com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair

interface TokenStore {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(
        tokenPair: TokenPair
    )

    fun clearTokens()
}