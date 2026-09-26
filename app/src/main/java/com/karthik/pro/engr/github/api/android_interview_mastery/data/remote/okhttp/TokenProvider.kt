package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore

object TokenProvider {
    val tokenStore: TokenStore by lazy {
        InMemoryTokenStore()
    }
}