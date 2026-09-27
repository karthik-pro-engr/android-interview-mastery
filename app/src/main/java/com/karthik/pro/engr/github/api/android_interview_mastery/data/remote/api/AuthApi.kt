package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.auth.TokenCredentialsDto
import retrofit2.Call
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/refresh")
    fun refreshToken(): Call<TokenCredentialsDto>
}