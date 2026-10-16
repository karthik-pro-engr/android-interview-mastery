package com.karthik.pro.engr.github.api.android_interview_mastery.data.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.DummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.login.LoginRequestDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.mapper.NetworkErrorMapper
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.AuthenticatedDummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.UserProfile
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.DummyJsonSessionRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NetworkUnavailable
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.SerializationError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unknown
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DummyJsonSessionRepositoryImpl @Inject constructor(
    @AuthenticatedDummyJsonApi
    private val api: DummyJsonApi,
    private val tokenStore: TokenStore
) : DummyJsonSessionRepository {

    override suspend fun login(
        username: String,
        password: String,
        expiresInMins: Int
    ): AppResult<Unit> {
        return try {
            val response = api.login(
                LoginRequestDto(
                    username = username,
                    password = password,
                    expiresInMins = expiresInMins
                )
            )

            if (!response.isSuccessful) {
                return Failure(NetworkErrorMapper.mapError(response.code()))
            }

            val body = response.body()
                ?: return Failure(SerializationError("Login response body was empty."))

            if (body.accessToken.isBlank() || body.refreshToken.isBlank()) {
                return Failure(SerializationError("Login response contained invalid tokens."))
            }

            withContext(Dispatchers.IO) {
                tokenStore.saveTokens(
                    TokenPair(
                        accessToken = body.accessToken,
                        refreshToken = body.refreshToken
                    )
                )
            }

            Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: IOException) {
            Failure(NetworkUnavailable)
        } catch (exception: SerializationException) {
            Failure(SerializationError(exception.message))
        } catch (_: Exception) {
            Failure(Unknown)
        }
    }

    override suspend fun getProfile(): AppResult<UserProfile> {
        return try {
            val response = api.getProfile()

            if (!response.isSuccessful) {
                return Failure(NetworkErrorMapper.mapError(response.code()))
            }

            val body = response.body()
                ?: return Failure(SerializationError("Profile response body was empty."))

            Success(
                UserProfile(
                    id = body.id,
                    firstName = body.firstName,
                    lastName = body.lastName,
                    username = body.username,
                    email = body.email
                )
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: IOException) {
            Failure(NetworkUnavailable)
        } catch (exception: SerializationException) {
            Failure(SerializationError(exception.message))
        } catch (_: Exception) {
            Failure(Unknown)
        }
    }
}