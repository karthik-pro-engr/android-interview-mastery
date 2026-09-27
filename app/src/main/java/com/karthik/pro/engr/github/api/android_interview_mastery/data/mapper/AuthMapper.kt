package com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.auth.TokenCredentialsDto
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.TokenCredentials

fun TokenCredentialsDto.toDomain(): TokenCredentials = TokenCredentials(
    accessToken = accessToken,
    refreshToken = refreshToken
)
