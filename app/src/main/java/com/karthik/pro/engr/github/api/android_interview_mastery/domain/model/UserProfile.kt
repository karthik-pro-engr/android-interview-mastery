package com.karthik.pro.engr.github.api.android_interview_mastery.domain.model

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}