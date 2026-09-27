package com.karthik.pro.engr.github.api.android_interview_mastery.di

import android.content.Context
import com.karthik.pro.engr.github.api.android_interview_mastery.BuildConfig
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.AuthApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.http.HttpHeaders
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp.AuthInterceptor
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp.TokenAuthenticator
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.MainClient
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.MainRetrofit
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.RefreshClient
import com.karthik.pro.engr.github.api.android_interview_mastery.di.qualifier.RefreshRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.github.com/"
    private const val CONNECT_TIMEOUT_SECONDS = 10L
    private const val WRITE_TIMEOUT_SECONDS = 30L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val CALL_TIMEOUT_SECONDS = 60L


    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache = Cache(
        directory = File(
            context.cacheDir,
            "http_cache"
        ),
        maxSize = 10L * 1024 * 1024
    )

    @Provides
    @Singleton
    @MainClient
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .cache(cache)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        redactHeader(HttpHeaders.AUTHORIZATION)
        redactHeader(HttpHeaders.COOKIE)
    }

    @Provides
    @Singleton
    @RefreshClient
    fun provideRefreshOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()


    @Provides
    @Singleton
    @RefreshRetrofit
    fun provideRefreshRetrofit(@RefreshClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                JsonProvider.converter()
            ).build()

    @Provides
    @Singleton
    fun provideAuthApi(@RefreshRetrofit retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainRetrofit(@MainClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JsonProvider.converter())
            .build()

    @Provides
    @Singleton
    fun provideGithubApi(@MainRetrofit retrofit: Retrofit): GithubApi =
        retrofit.create(GithubApi::class.java)
}