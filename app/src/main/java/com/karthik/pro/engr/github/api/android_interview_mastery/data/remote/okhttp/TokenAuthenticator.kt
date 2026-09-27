package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.AuthApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.http.HttpHeaders
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject


class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    private val authApi: AuthApi
) : Authenticator {
    private val lock = Any()
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.priorResponse != null) {
            return null
        }

        val requestAccessToken =
            response.request.header("Authorization")?.removePrefix("Bearer ") ?: return null

        val currentAccessToken = tokenStore.getAccessToken() ?: return null



        if (requestAccessToken == currentAccessToken) {
            synchronized(lock) {
                val localToken = tokenStore.getAccessToken() ?: return null
                if (requestAccessToken == localToken) {

                    val refreshResponse = try {
                        authApi.refreshToken().execute()
                    } catch (_: IOException) {
                        return null
                    }

                    if (refreshResponse.isSuccessful) {

                        val tokenCredentialsDto = refreshResponse.body() ?: return null

                        tokenStore.saveTokens(
                            tokenCredentialsDto.accessToken,
                            tokenCredentialsDto.refreshToken
                        )

                        val newToken = tokenStore.getAccessToken() ?: return null

                        return buildRequest(response, newToken)
                    } else {
                        return null
                    }

                } else {
                    val newToken = tokenStore.getAccessToken() ?: return null
                    return buildRequest(response, newToken)
                }
            }
        } else {
            val newToken = tokenStore.getAccessToken() ?: return null
            return buildRequest(response, newToken)
        }

    }

    private fun buildRequest(response: Response, newToken: String): Request =
        response.request.newBuilder()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $newToken").build()
}