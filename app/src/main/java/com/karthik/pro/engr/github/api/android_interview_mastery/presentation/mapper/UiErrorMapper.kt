package com.karthik.pro.engr.github.api.android_interview_mastery.presentation.mapper

import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.model.UiMessage
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

fun AppError.toUiErrorMessage(): UiMessage =
    when (this) {
        is InvalidRequest ->
            UiMessage("Please check the issue details.")

        Unauthorized ->
            UiMessage("Please sign in again.")

        Forbidden ->
            UiMessage("You don't have permission to perform this action.")

        NotFound ->
            UiMessage("The requested resource was not found.")

        RateLimited ->
            UiMessage("Too many requests. Please try again later.")

        ServerError ->
            UiMessage("Something went wrong on the server.")

        NetworkUnavailable ->
            UiMessage("Please check your internet connection.")

        is SerializationError ->
            UiMessage("We couldn't process the server response.")

        UnknownOperationOutcome ->
            UiMessage("We couldn't confirm whether the operation completed.")

        Unknown ->
            UiMessage("Something went wrong.")

    }