package com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.TokenCredentials

interface AuthRepository {
    fun refreshToken(refreshToken: String): TokenCredentials
}