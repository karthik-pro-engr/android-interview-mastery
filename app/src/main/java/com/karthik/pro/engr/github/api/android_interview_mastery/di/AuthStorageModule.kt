package com.karthik.pro.engr.github.api.android_interview_mastery.di

import com.karthik.pro.engr.github.api.android_interview_mastery.data.auth.SecureTokenStoreImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthStorageModule {

    @Binds
    @Singleton
    abstract fun bindTokenStore(
        implementation: SecureTokenStoreImpl
    ): TokenStore
}