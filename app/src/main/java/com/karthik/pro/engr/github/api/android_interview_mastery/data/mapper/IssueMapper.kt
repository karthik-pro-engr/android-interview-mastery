package com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.CreateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueUserDto
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Issue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.IssueUser

fun CreateIssue.toDto(): CreateIssueRequest = CreateIssueRequest(
    body = body,
    title = title
)

fun IssueUserDto.toDomain(): IssueUser = IssueUser(
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
    id = id,
    login = login
)

fun IssueDto.toDomain(): Issue = Issue(
    body = body,
    createdAt = createdAt,
    htmlUrl = htmlUrl,
    id = id,
    number = number,
    state = state,
    title = title,
    updatedAt = updatedAt,
    user = user.toDomain()
)