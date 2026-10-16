package com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.UserProfile
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult

interface DummyJsonSessionRepository {

    suspend fun login(
        username: String,
        password: String,
        expiresInMins: Int = 30
    ): AppResult<Unit>

    suspend fun getProfile(): AppResult<UserProfile>
}