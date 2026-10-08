package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.profile

import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)