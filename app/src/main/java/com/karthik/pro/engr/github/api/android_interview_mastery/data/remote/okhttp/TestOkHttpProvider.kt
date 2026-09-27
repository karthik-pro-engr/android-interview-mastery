package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.http.HttpHeaders
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object TestOkHttpProvider {


    private const val CONNECT_TIMEOUT = 10L

    private const val WRITE_TIMEOUT = 30L

    private const val READ_TIMEOUT = 30L

    private const val CALL_TIMEOUT = 60L

    val logging: HttpLoggingInterceptor by lazy {

        HttpLoggingInterceptor().apply {

            level = if (BuildConfig.DEBUG) {

                HttpLoggingInterceptor.Level.BODY

            } else HttpLoggingInterceptor.Level.NONE

        }

    }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->

                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer summar")
                        .build()
                )
            }
            .addInterceptor(logging)
            .build()
    }
}