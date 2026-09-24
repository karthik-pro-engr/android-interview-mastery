package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Test

class GithubApiTest {
    @Test
    fun testGithubRepositories() = runBlocking {
        val response = RetrofitProvider.githubApi.repos(
            username = "karthik-pro-engr",
            sort = "stars",
            order = "desc"
        )

        println(response)
    }
}