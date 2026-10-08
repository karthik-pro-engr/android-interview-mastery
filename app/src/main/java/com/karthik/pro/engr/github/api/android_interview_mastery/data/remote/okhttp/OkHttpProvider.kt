package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import android.content.Context
import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.RetrofitProvider
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.http.HttpHeaders
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit


object OkHttpProvider {
    private const val CONNECT_TIMEOUT_SECONDS = 10L
    private const val WRITE_TIMEOUT_SECONDS = 30L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val CALL_TIMEOUT_SECONDS = 60L

    val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else HttpLoggingInterceptor.Level.NONE

            redactHeader(HttpHeaders.AUTHORIZATION)
            redactHeader(HttpHeaders.COOKIE)
        }
    }

    fun createCache(context: Context): Cache {
        val directory = File(
            context.cacheDir,
            "http_cache"
        )

        return Cache(
            directory = directory,
            maxSize = 10L * 1024 * 1024
        )
    }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${BuildConfig.GITHUB_AIM_TOKEN}"
                        ).build()
                )
            }
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .authenticator(TokenAuthenticator(TokenProvider.tokenStore, RetrofitProvider.dummyJsonApi))
            //.cache(createCache())
            .build()
    }

    val refreshClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}