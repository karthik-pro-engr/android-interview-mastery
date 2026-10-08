package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.DummyJsonApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.http.HttpHeaders
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TestTokenAuthenticator(private val tokenStore: TokenStore, private val dummyJsonApi: DummyJsonApi) :
    Authenticator {
    private val lock = Any()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) {
            return null
        }
        val oldAccessToken = tokenStore.getAccessToken() ?: return null

        val requestAccessToken =
            response.request.header(HttpHeaders.AUTHORIZATION)?.removePrefix("Bearer ")
                ?: return null

        if (oldAccessToken == requestAccessToken) { // both are same so have to refresh the authentication toke
            synchronized(lock) { // lock the process until release it for other calls

                val savedAccessToken = tokenStore.getAccessToken() ?: return null

                if (requestAccessToken == savedAccessToken) {
                    val refreshResponse = try {
                        dummyJsonApi.refreshToken().execute()
                    } catch (ex: Exception) {
                        return null
                    }

                    if (refreshResponse.isSuccessful) {
                        val tokenCredentialsDto = refreshResponse.body() ?: return null
                        tokenStore.saveTokens(
                            tokenCredentialsDto.accessToken,
                            tokenCredentialsDto.refreshToken
                        )
                        val savedAccessToken = tokenStore.getAccessToken() ?: return null
                        return updatedRequest(response, savedAccessToken)
                    } else
                        return null

                } else {
                    val savedAccessToken = tokenStore.getAccessToken() ?: return null
                    return updatedRequest(response, savedAccessToken)
                }

            }
        } else {
            val savedAccessToken = tokenStore.getAccessToken() ?: return null
            return updatedRequest(response, savedAccessToken)
        }


    }

    private fun updatedRequest(response: Response, savedAccessToken: String): Request {
        return response.request.newBuilder()
            .addHeader(HttpHeaders.AUTHORIZATION, savedAccessToken)
            .build()
    }
}