package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.CreateIssueRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.ReplaceIssueRequest
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

/*GET
/repos/{owner}/{repo}/issues/{issue_number}

POST
/repos/{owner}/{repo}/issues

PUT
/repos/{owner}/{repo}/issues/{issue_number}

PATCH
/repos/{owner}/{repo}/issues/{issue_number}

DELETE
/repos/{owner}/{repo}/issues/{issue_number}

Constraints
All operations require Authorization.
GET has no body.
POST sends CreateIssueRequest.
PUT sends the complete replacement representation.
PATCH sends only fields being changed.
DELETE doesn't require a request body.
For mutation operations, assume the caller needs HTTP status information.
GET should return the issue directly.
*/
interface GithubIssueApi {

    // 1. Get issue
    @GET("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun getIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Header("Authorization") authorization: String
    ): IssueDto

    // 2. Create issue
    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Header("Authorization") authorization: String,
        @Body issue: CreateIssueRequest
    ): Response<IssueDto>

    // 3. Replace issue
    @PUT("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun replaceIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Header("Authorization") authorization: String,
        @Body replaceIssue: ReplaceIssueRequest
    ): Response<IssueDto>


    // 4. Partially update issue
    @PATCH("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun updateIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Header("Authorization") authorization: String,
        @Body updateIssue: UpdateIssueRequest
    ): Response<IssueDto>


    // 5. Delete issue
    @DELETE("repos/{owner}/{repo}/issues/{issue_number}")
    suspend fun deleteIssue(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String,
        @Path("issue_number") issueNumber: String,
        @Header("Authorization") authorization: String
    ): Response<Unit>


}