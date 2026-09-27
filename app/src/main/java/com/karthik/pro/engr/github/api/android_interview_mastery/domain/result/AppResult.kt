package com.karthik.pro.engr.github.api.android_interview_mastery.domain.result

sealed interface AppResult<out T>

data class Success<T>(
    val data: T
) : AppResult<T>

data class Failure(
    val error: AppError
) : AppResult<Nothing>