package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response

data class IssueDto(
    val body: String,
    val created_at: String,
    val html_url: String,
    val id: Int,
    val number: Int,
    val state: String,
    val title: String,
    val updated_at: String,
    val user: IssueUserDto
)