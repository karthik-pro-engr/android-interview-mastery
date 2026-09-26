package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp.OkHttpProvider
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitProvider {
    private const val BASE_URL = "https://api.github.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpProvider.client)
            .addConverterFactory(
                JsonProvider.json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    val githubApi: GithubApi by lazy {
        retrofit.create(GithubApi::class.java)
    }
}