package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import okhttp3.OkHttpClient

object OkHttpProvider {

    val client: OkHttpClient by lazy {
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
    }
}