package com.karthik.pro.engr.github.api.android_interview_mastery.data.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.DummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.auth.RefreshTokenRequestDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.mapper.NetworkErrorMapper
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.RefreshDummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.TokenCredentials
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.AuthRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NetworkUnavailable
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.SerializationError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unknown
import kotlinx.serialization.SerializationException
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @RefreshDummyJsonApi
    private val api: DummyJsonApi
) : AuthRepository {

    override fun refreshToken(
        refreshToken: String
    ): AppResult<TokenCredentials> {
        return try {
            val response = api.refreshToken(
                RefreshTokenRequestDto(
                    expiresInMins = 30,
                    refreshToken = refreshToken
                )
            )

            if (!response.isSuccessful) {
                return Failure(NetworkErrorMapper.mapError(response.code()))
            }

            val body = response.body()
                ?: return Failure(SerializationError("Refresh response body was empty."))

            if (body.accessToken.isBlank() || body.refreshToken.isBlank()) {
                return Failure(SerializationError("Refresh response contained invalid tokens."))
            }

            Success(
                TokenCredentials(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken
                )
            )
        } catch (_: IOException) {
            Failure(NetworkUnavailable)
        } catch (exception: SerializationException) {
            Failure(SerializationError(exception.message))
        } catch (_: Exception) {
            Failure(Unknown)
        }
    }
}