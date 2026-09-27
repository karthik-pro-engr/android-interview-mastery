package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import javax.inject.Inject

class InMemoryTokenStoreImpl @Inject constructor() : TokenStore {

    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun getAccessToken(): String? = accessToken

    override fun getRefreshToken(): String? = refreshToken

    override fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}