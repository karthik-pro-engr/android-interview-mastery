package com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Issue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.IssueUser
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NetworkUnavailable
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CreateIssueUseCaseTest {
    private lateinit var repository: GithubApiRepository
    private lateinit var useCase: CreateIssueUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = CreateIssueUseCase(
            githubApiRepository = repository
        )
    }

    @Test
    fun createIssue_whenCallingCreateIssue_returnSuccess() = runTest {
        //        Arrange
        val issue = Issue(
            body = null,
            createdAt = "",
            htmlUrl = "",
            id = 1,
            number = 2222,
            state = "",
            title = "Issue1",
            updatedAt = "",
            user = IssueUser(
                avatarUrl = "",
                htmlUrl = "",
                id = 3,
                login = ""
            )
        )
        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )
        val expected = Success(
            issue
        )

        whenever(
            repository.createIssue(
                owner = eq("karthik-pro-engr"),
                repo = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(expected)

        val result = useCase(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            issue = createIssue
        )

        assertEquals(expected, result)

        verify(repository).createIssue(
            owner = eq("karthik-pro-engr"),
            repo = eq("android-interview-mastery"),
            idempotencyKey = any(),
            issue = eq(createIssue)
        )

    }

    @Test
    fun createIssue_whenCallingCreateIssue_returnFailure() = runTest {
        //        Arrange
        val createIssue = CreateIssue(
            title = "Issue1",
            body = ""
        )
        val expected = Failure(NetworkUnavailable)

        whenever(
            repository.createIssue(
                owner = eq("karthik-pro-engr"),
                repo = eq("android-interview-mastery"),
                idempotencyKey = any(),
                issue = any()
            )
        ).thenReturn(expected)

        val result = useCase(
            owner = "karthik-pro-engr",
            repo = "android-interview-mastery",
            issue = createIssue
        )

        assertEquals(expected, result)

        verify(repository).createIssue(
            owner = eq("karthik-pro-engr"),
            repo = eq("android-interview-mastery"),
            idempotencyKey = any(),
            issue = eq(createIssue)
        )

    }
}