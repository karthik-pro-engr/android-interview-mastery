package com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization

import kotlinx.serialization.json.Json

object JsonProvider {
    val json = Json {
        ignoreUnknownKeys = true
    }
}