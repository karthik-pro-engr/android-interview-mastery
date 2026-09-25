package com.karthik.pro.engr.github.api.android_interview_mastery.domain.result

sealed interface AppError

object NetworkUnavailable : AppError
object Unauthorized : AppError
object Forbidden : AppError
object NotFound : AppError
object RateLimited : AppError
object ServerError : AppError
object InvalidData : AppError

data class SerializationError(
    val message: String? = null
) : AppError

data class ApiError(
    val message: String
) : AppError

object UnknownOperationOutcome : AppError
object Unknown : AppError