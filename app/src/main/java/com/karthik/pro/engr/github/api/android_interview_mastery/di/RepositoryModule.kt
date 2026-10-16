package com.karthik.pro.engr.github.api.android_interview_mastery.di

import com.karthik.pro.engr.github.api.android_interview_mastery.data.repository.AuthRepositoryImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.data.repository.DummyJsonSessionRepositoryImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.data.repository.GithubApiRepositoryImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.AuthRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.DummyJsonSessionRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(
        implementation: GithubApiRepositoryImpl
    ): GithubApiRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDummyJsonSessionRepository(
        implementation: DummyJsonSessionRepositoryImpl
    ): DummyJsonSessionRepository
}