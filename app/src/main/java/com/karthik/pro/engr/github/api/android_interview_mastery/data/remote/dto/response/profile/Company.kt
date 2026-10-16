package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.profile

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val address: Address,
    val department: String,
    val name: String,
    val title: String
)