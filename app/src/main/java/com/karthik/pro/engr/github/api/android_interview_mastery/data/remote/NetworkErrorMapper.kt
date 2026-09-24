package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Forbidden
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NotFound
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.RateLimited
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.ServerError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unauthorized
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unknown


object NetworkErrorMapper {
    fun mapError(code: Int): AppError = when (code) {
        401 -> Unauthorized
        403 -> Forbidden
        404 -> NotFound
        429 -> RateLimited
        in 500..599 -> ServerError
        else -> Unknown
    }
}