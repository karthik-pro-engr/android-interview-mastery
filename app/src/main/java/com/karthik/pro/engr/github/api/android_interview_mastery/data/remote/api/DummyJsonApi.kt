package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.auth.RefreshTokenRequestDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.login.LoginRequestDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.auth.RefreshTokenResponseDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.login.LoginResponseDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.profile.ProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DummyJsonApi {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequestDto: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("auth/refresh")
     fun refreshToken(
        @Body refreshTokenRequestDto: RefreshTokenRequestDto
    ): Response<RefreshTokenResponseDto>

    @GET("auth/me")
    suspend fun getProfile(): Response<ProfileResponseDto>

}