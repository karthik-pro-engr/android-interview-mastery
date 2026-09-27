package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val accessToken = tokenStore.getAccessToken() ?: return chain.proceed(request)

        if (accessToken.isEmpty()) {
            return chain.proceed(request)
        }

        return chain.proceed(
            request.newBuilder()
                .header(
                    "Authorization",
                    "Bearer $accessToken"
                ).build()
        )
    }
}