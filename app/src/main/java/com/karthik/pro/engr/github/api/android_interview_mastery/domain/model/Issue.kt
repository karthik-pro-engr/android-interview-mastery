package com.karthik.pro.engr.github.api.android_interview_mastery.domain.model

data class Issue(
    val body: String?,
    val createdAt: String,
    val htmlUrl: String,
    val id: Int,
    val number: Int,
    val state: String,
    val title: String,
    val updatedAt: String,
    val user: IssueUser
)