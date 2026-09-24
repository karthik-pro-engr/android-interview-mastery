package com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult

interface GithubApiRepository {
    suspend fun getRepos(
        username: String,
        sort: String,
        order: String,
        header: String
    ): AppResult<List<Repo>>

}