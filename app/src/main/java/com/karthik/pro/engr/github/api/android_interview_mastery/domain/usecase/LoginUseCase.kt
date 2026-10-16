package com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.DummyJsonSessionRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: DummyJsonSessionRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): AppResult<Unit> =
        repository.login(username, password)
}