package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import android.R
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.CreateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.ReplaceIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.UpdateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface TestGithubApi {
    /*
    1. Get repositories
    GET /users/{username}/repos
      sort and order are query parameters
     */

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") userName: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): List<GitHubRepoDto>

    /**
     * 2. Create issue
     * POST /repos/{owner}/{repo}/issues
     * {
     *   "title": "...",
     *   "body": "..."
     * }
     */

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssues(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Body issue: CreateIssueRequest
    ): Response<IssueDto>

    /*
    * 3. Replace issue
    * PUT /repos/{owner}/{repo}/issues/{issue_number}
    * */

    @PUT("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun replaceIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Body issue: ReplaceIssueRequest
    ): Response<IssueDto>

    /*
    * 4. Partially update issue
    * PATCH /repos/{owner}/{repo}/issues/{issue_number}
    * */

    @PATCH("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun updateIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Body issue: UpdateIssueRequest
    ): Response<IssueDto>

    /*
    * 5. Delete issue
    * DELETE /repos/{owner}/{repo}/issues/{issue_number}
    * */

    @DELETE("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun deleteIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String
    ): Response<Unit>

    /*
    * 6. Dynamic endpoint
    * The server gives you:
    * https://api.github.com/repositories?page=2
    *  */
    @GET
    suspend fun dynamicUrl(
        @Url url: String
    ): Response<Unit>

}