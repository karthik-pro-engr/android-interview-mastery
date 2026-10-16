package com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.UserProfile
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.DummyJsonSessionRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: DummyJsonSessionRepository
) {
    suspend operator fun invoke(): AppResult<UserProfile> =
        repository.getProfile()
}