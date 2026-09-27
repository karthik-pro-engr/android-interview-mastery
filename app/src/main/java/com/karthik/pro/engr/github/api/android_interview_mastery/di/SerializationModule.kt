package com.karthik.pro.engr.github.api.android_interview_mastery.di

import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Provides
    @Singleton
    fun provideJson(): Json = JsonProvider.json
}