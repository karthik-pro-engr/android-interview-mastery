package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Forbidden
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.InvalidRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NetworkUnavailable
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NotFound
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.RateLimited
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.SerializationError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.ServerError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unauthorized
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unknown
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.UnknownOperationOutcome

fun AppError.toUserMessage(): String = when (this) {
    NetworkUnavailable ->
        "Network unavailable. Check your connection and try again."

    Unauthorized ->
        "Your session is no longer valid. Please sign in again."

    Forbidden ->
        "You don't have permission to perform this action."

    NotFound ->
        "The requested resource could not be found."

    RateLimited ->
        "Too many requests. Please wait before trying again."

    ServerError ->
        "The server encountered a problem. Please try again later."

    is SerializationError ->
        "We couldn't read the server response."

    is InvalidRequest ->
        message

    UnknownOperationOutcome ->
        "We couldn't confirm the operation's result. Check its status before retrying."

    Unknown ->
        "Something went wrong. Please try again."
}