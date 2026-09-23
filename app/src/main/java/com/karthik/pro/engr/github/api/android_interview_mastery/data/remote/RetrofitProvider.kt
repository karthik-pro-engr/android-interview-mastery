package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import retrofit2.Retrofit

object RetrofitProvider {
    private const val BASE_URL = "https://api.github.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
    }

    val githubApi: GithubApi by lazy {
        retrofit.create(GithubApi::class.java)
    }
}