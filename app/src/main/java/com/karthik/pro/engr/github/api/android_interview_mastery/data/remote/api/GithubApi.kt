package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("users/karthik-pro-engr/repos")
    suspend fun repos(
        @Query("sort") sort:String,
        @Query("order") order: String
    ): List<GitHubRepoDto>
}