package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore
) : Interceptor {

    companion object {
        const val SKIP_AUTH_HEADER = "X-Skip-Authorization"
        private const val SKIP_AUTH_VALUE = "true"
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Check whether this request explicitly skips automatic auth.
        val skipAuthorization = originalRequest
            .header(SKIP_AUTH_HEADER)
            ?.equals(SKIP_AUTH_VALUE, ignoreCase = true) == true

        // Never send our internal control header to the server.
        val request = originalRequest.newBuilder()
            .removeHeader(SKIP_AUTH_HEADER)
            .build()

        if (skipAuthorization) {
            return chain.proceed(request)
        }

        // Preserve an Authorization header supplied by the caller.
        if (request.header(AUTHORIZATION_HEADER) != null) {
            return chain.proceed(request)
        }

        val accessToken = tokenStore.getAccessToken()
            ?.takeIf { it.isNotBlank() }
            ?: return chain.proceed(request)

        val authenticatedRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer $accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}