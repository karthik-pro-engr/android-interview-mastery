package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.profile

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val address: String,
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val postalCode: String,
    val state: String,
    val stateCode: String
)