package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users/{username}/repos")
    suspend fun repos(
        @Path("username") username:String,
        @Query("sort") sort:String,
        @Query("order") order: String,
        @Header("Authorization") authorization: String
    ): List<GitHubRepoDto>
}