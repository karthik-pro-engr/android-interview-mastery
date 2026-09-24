package com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubOwnerDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.RepoOwner

fun GitHubRepoDto.toDomain(): Repo = Repo(
    id = id,
    name = name,
    description = description,
    language = language,
    stars = stargazersCount,
    forks = forksCount,
    owner = owner.toDomain()
)

fun GitHubOwnerDto.toDomain(): RepoOwner = RepoOwner(
    name = login,
    avatarUrl = avatarUrl
)