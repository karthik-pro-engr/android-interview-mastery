package com.karthik.pro.engr.github.api.android_interview_mastery.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper.toDomain
import com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper.toDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.response.IssueUserDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.serialization.JsonProvider
import com.karthik.pro.engr.github.api.android_interview_mastery.data.repository.GithubApiRepositoryImpl
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.InvalidRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NotFound
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.SerializationError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.UnknownOperationOutcome
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

class GithubApiRepositoryTest {
    private lateinit var api: GithubApi
    private lateinit var json: Json
    private lateinit var repository: GithubApiRepositoryImpl



    @Before
    fun setup() {
        api = mock()
        json = JsonProvider.json

        repository = GithubApiRepositoryImpl(api, json)
    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsSuccessResponse() = runTest {

//        Arrange

        val issueDto = IssueDto(
            body = "body",
            createdAt = "",
            htmlUrl = "",
            id = 123,
            number = 111,
            state = "",
            title = "",
            updatedAt = "",
            user = IssueUserDto(
                avatarUrl = "",
                htmlUrl = "",
                id = 12344,
                login = ""
            )
        )

        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )

        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(Response.success(issueDto))

//        Act
        val appResult = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(appResult, Success(issueDto.toDomain()))

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsFailureWithSerializationError() = runTest {

//        Arrange


        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )

        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(Response.success(null))

//        Act
        val appResult = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(appResult, Failure(SerializationError()))

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsErrorBodyNullForUnsuccessResponse() = runTest {

//        Arrange


        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )

        val response = mock<Response<IssueDto>>()
        whenever(response.isSuccessful).thenReturn(false)
        whenever(response.code()).thenReturn(404)
        whenever(response.errorBody()).thenReturn(null)

        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(response)

//        Act
        val actual = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(Failure(NotFound), actual)

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsErrorBodyUnsuccessResponse() = runTest {

//        Arrange


        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )

        val errorJson = """{"message": "input id is not valid"}"""
        val errorResponseBody = errorJson.toResponseBody("application/json".toMediaType())


        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(Response.error(404, errorResponseBody))

//        Act
        val actual = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(Failure(InvalidRequest("input id is not valid")), actual)

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsInvalidErrorBodyMappedError() = runTest {

//        Arrange


        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )

        val errorJson = """{"message": "input id is not valid""""
        val errorResponseBody = errorJson.toResponseBody("application/json".toMediaType())


        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(Response.error(404, errorResponseBody))

//        Act
        val actual = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(Failure(NotFound), actual)

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }

    @Test
    fun createIssue_whenCallingPostRequest_returnsUnknownOperationOutcome() = runTest {

//        Arrange


        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )



        whenever(
            api.createIssue(
                ownerName = eq("karthik-pro-engr"),
                repoName = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenThrow(IOException("Network failure"))

//        Act
        val actual = repository.createIssue(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            idempotencyKey = "test-idempotency-key",
            issue = createIssue
        )

        // Assert
        assertEquals(Failure(UnknownOperationOutcome), actual)

        // Verify
        verify(api).createIssue(
            ownerName = eq("karthik-pro-engr"),
            repoName = eq("android-interview-mastery"),
            idempotencyKey = eq("test-idempotency-key"),
            issue = eq(createIssue.toDto())
        )


    }


}