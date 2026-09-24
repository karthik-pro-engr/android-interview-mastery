package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.CreateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.UpdateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users/{username}/repos")
    suspend fun repos(
        @Path("username") username: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Header("Authorization") authorization: String
    ): List<GitHubRepoDto>

    // POST /repos/{owner}/{repo}/issues
    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Body issue: CreateIssueRequest
    ): Response<IssueDto>

    //PUT /repos/{owner}/{repo}/issues/{issue_number}

    @PUT("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun updateIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") number: String,
        @Body updateIssue: UpdateIssueRequest
    ): Response<IssueDto>

    // @PATCH "repos/{owner}/{repo}/issues/{issue_number}"
    @PATCH("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun patchIssue(
        @Path("owner") ownerName:String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Body updateIssue:UpdateIssueRequest
    ): Response<IssueDto>

    // DELETE /repos/{owner}/{repo}/issues/{issue_number}
    @DELETE("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun deleteIssue(
        @Path("owner") ownerName:String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String
    ): Response<Unit>
}