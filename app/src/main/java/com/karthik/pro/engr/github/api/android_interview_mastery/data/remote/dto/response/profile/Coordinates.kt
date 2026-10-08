package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.profile

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lat: Double,
    val lng: Double
)