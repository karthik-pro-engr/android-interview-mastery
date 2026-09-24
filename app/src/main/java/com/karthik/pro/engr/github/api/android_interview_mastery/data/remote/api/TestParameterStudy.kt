package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.GitHubRepoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


/**
You're implementing the GitHub repository listing API for our real Android project.

Business requirement

The repository screen needs to:

Load repositories for a given GitHub username, optionally sort them, optionally paginate them, and send the authenticated user's token.

The backend contract is:

GET /users/{username}/repos

Example request:

GET /users/karthik-pro-engr/repos?sort=stars&order=desc&page=2&per_page=30
Authorization: Bearer <token>
Contract rules
username → required
sort → optional
order → optional
page → optional
per_page → required
Authorization → required
Response → List<GitHubRepoDto>
Production constraint

The API may later add many optional query filters, so use @QueryMap where it makes sense rather than blindly making every query parameter an individual argument.

Your task

From a blank screen, write the complete Retrofit method:

interface GithubApi {

// your method here
}

You need to decide:

HTTP annotation
Path parameter
Query vs QueryMap
Nullable vs non-null parameters
Header
Return type
Any supporting type you need for @QueryMap

Don't look back at our previous code. Rebuild it from the HTTP contract.
 * */

interface TestParameterStudy {

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String,
        @Query("per_page") perPage:Int,
        @QueryMap params: Map<String, String?>,
        @Header("Authorization") authorization: String
    ): List<GitHubRepoDto>


}