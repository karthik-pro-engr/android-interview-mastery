package com.karthik.pro.engr.github.api.android_interview_mastery.presentation

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.RepoOwner
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.CreateIssueUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase.GetReposListUseCase
import com.karthik.pro.engr.github.api.android_interview_mastery.presentation.github.repos.GithubUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.milliseconds

class GithubViewModelTest {

    private lateinit var createIssueUseCase: CreateIssueUseCase
    private lateinit var getReposListUseCase: GetReposListUseCase

    private lateinit var githubViewModel: GithubViewModel

    @get:Rule
    private val dispatcher = MainDispatcherRule()

    @Before
    fun setup() {
        createIssueUseCase = mock()
        getReposListUseCase = mock()
        githubViewModel =
            GithubViewModel(createIssueUseCase, getReposListUseCase, dispatcher.dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onUsernameChanged_whenValidUsername_returnsSuccess() = runTest {
        val repos = mutableListOf<Repo>()
        for (i in 1..5) {
            repos.add(
                Repo(
                    id = i.toLong(),
                    name = "$i",
                    description = "$i",
                    language = "kotlin",
                    stars = 10,
                    forks = 1,
                    owner = RepoOwner("Karthik", null)
                )
            )
        }
        val githubUiStateSuccess = GithubUiState.Success(repos)

        // Arrange
        whenever(
            getReposListUseCase(
                username = eq("karthik-pro-engr"),
                sort = eq("updated"),
                order = eq("desc"),
                header = eq("")
            )
        ).thenReturn(Success<List<Repo>>(repos))

        // Act

        githubViewModel.onUsernameChanged("karthik-pro-engr")

        advanceTimeBy(500.milliseconds)

        advanceUntilIdle()


        // Assert

      //  assertEquals(githubUiStateSuccess, githubViewModel.uiState.value)

        verify(getReposListUseCase).invoke(
            username = eq("karthik-pro-engr"),
            sort = eq("updated"),
            order = eq("desc"),
            header = eq("")
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onUsernameChanged_whenUsernameIsLessThan3_doesNotMakeUseCase() = runTest {
        val repos = mutableListOf<Repo>()
        for (i in 1..5) {
            repos.add(
                Repo(
                    id = i.toLong(),
                    name = "$i",
                    description = "$i",
                    language = "kotlin",
                    stars = 10,
                    forks = 1,
                    owner = RepoOwner("Karthik", null)
                )
            )
        }
        val githubUiStateSuccess = GithubUiState.Success(repos)

        // Arrange
        whenever(
            getReposListUseCase(
                username = eq("karthik-pro-engr"),
                sort = eq("updated"),
                order = eq("desc"),
                header = eq("")
            )
        ).thenReturn(Success<List<Repo>>(repos))

        // Act

        githubViewModel.onUsernameChanged("ka")

        advanceTimeBy(500.milliseconds)

        advanceUntilIdle()


        // Assert

        assertEquals(GithubUiState.Loading, githubViewModel.uiState.value)

        verify(getReposListUseCase, never()).invoke(
            username = eq("karthik-pro-engr"),
            sort = eq("updated"),
            order = eq("desc"),
            header = eq("")
        )
    }

}