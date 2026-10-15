package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.TokenCredentials
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.AuthRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    private val authRepository: AuthRepository
) : Authenticator {

    private val lock = Any()

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Do not retry an already retried request indefinitely.
        if (response.priorResponse != null) return null

        val requestAccessToken = response.request
            .header("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.takeIf { it.isNotBlank() }
            ?: return null

        val currentAccessToken =
            tokenStore.getAccessToken() ?: return null

        // Another request already refreshed the access token.
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

            val credentials = when (
                val result = authRepository.refreshToken(refreshToken)
            ) {
                is Success -> result.data
                is Failure -> return null
            }

            tokenStore.saveTokens(
                TokenPair(
                    accessToken = credentials.accessToken,
                    refreshToken = credentials.refreshToken
                )
            )

            return buildRequest(response, credentials.accessToken)
        }
    }

    private fun buildRequest(
        response: Response,
        accessToken: String
    ): Request =
        response.request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
}