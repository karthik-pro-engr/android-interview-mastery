package com.karthik.pro.engr.github.api.android_interview_mastery.di

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.okhttp.InMemoryTokenStoreImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindTokenStore(tokenStoreImpl: InMemoryTokenStoreImpl): TokenStore
}