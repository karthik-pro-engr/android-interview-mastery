package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object JsonProvider {
    val json = Json {
        ignoreUnknownKeys = true
    }
    fun converter(): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }
}