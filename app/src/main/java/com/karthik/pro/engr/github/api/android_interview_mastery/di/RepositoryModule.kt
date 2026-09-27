package com.karthik.pro.engr.github.api.android_interview_mastery.di

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import com.karthik.pro.engr.github.api.android_interview_mastery.data.repository.GithubApiRepositoryImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(githubRepository: GithubApiRepositoryImpl): GithubApiRepository
}