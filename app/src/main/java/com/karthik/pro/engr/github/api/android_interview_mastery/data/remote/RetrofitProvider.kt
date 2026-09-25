package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote

import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitProvider {
    private const val BASE_URL = "https://api.github.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(
                            chain.request().newBuilder().addHeader(
                                "Authorization",
                                "Bearer ${BuildConfig.GITHUB_AIM_TOKEN}"
                            ).build()
                        )
                    }
                    .build()
            )
            .addConverterFactory(
                JsonProvider.json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    val githubApi: GithubApi by lazy {
        retrofit.create(GithubApi::class.java)
    }
}