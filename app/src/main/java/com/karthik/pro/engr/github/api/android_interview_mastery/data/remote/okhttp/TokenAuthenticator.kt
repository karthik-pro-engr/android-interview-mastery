package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.DummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.request.auth.RefreshTokenRequestDto
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    private val dummyJsonApi: DummyJsonApi
) : Authenticator {

    private val lock = Any()

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Prevent an authentication retry loop.
        if (response.priorResponse != null) {
            return null
        }

        val requestAccessToken = response.request
            .header("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.takeIf { it.isNotBlank() }
            ?: return null

        val currentAccessToken =
            tokenStore.getAccessToken() ?: return null

        // Another request already refreshed the token.
        if (requestAccessToken != currentAccessToken) {
            return buildRequest(response, currentAccessToken)
        }

        synchronized(lock) {
            // Recheck after acquiring the lock.
            val latestAccessToken =
                tokenStore.getAccessToken() ?: return null

            if (requestAccessToken != latestAccessToken) {
                return buildRequest(response, latestAccessToken)
            }

            val refreshToken =
                tokenStore.getRefreshToken() ?: return null

            val refreshResponse = try {
                dummyJsonApi.refreshToken(
                    RefreshTokenRequestDto(
                        expiresInMins = 30,
                        refreshToken = refreshToken
                    )
                )
            } catch (exception: IOException) {
                return null
            }

            if (!refreshResponse.isSuccessful) {
                return null
            }

            val refreshedTokens =
                refreshResponse.body() ?: return null

            val newAccessToken = refreshedTokens.accessToken
                .takeIf { it.isNotBlank() }
                ?: return null

            val newRefreshToken = refreshedTokens.refreshToken
                .takeIf { it.isNotBlank() }
                ?: return null

            tokenStore.saveTokens(
                TokenPair(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken
                )
            )

            return buildRequest(response, newAccessToken)
        }
    }

    private fun buildRequest(
        response: Response,
        accessToken: String
    ): Request {
        return response.request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}