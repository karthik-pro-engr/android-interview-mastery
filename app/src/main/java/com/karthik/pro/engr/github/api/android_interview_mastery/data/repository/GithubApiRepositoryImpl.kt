package com.karthik.pro.engr.github.api.android_interview_mastery.data.repository

import com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper.toDomain
import com.karthik.pro.engr.github.api.android_interview_mastery.data.mapper.toDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.api.GithubApi
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.dto.error.GitHubErrorDto
import com.karthik.pro.engr.github.api.android_interview_mastery.data.remote.mapper.NetworkErrorMapper
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Issue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Failure
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.InvalidRequest
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.NetworkUnavailable
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.SerializationError
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Success
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.Unknown
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.UnknownOperationOutcome
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.IOException
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

class GithubApiRepositoryImpl(
    private val api: GithubApi,
    private val json: Json
) : GithubApiRepository {
    override suspend fun getRepos(
        username: String,
        sort: String,
        order: String,
        header: String
    ): AppResult<List<Repo>> {

        return try {
            Success(api.repos(username, sort, order, header).map { it.toDomain() })
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: HttpException) {
            Failure(NetworkErrorMapper.mapError(ex.code()))
        } catch (_: IOException) {
            Failure(NetworkUnavailable)
        } catch (_: SerializationException) {
            Failure(SerializationError())
        } catch (_: Exception) {
            Failure(Unknown)
        }
    }

    override suspend fun createIssue(
        owner: String,
        repo: String,
        idempotencyKey: String,
        issue: CreateIssue
    ): AppResult<Issue> {

        return try {
            val response = api.createIssue(
                ownerName = owner,
                repoName = repo,
                idempotencyKey = idempotencyKey,
                issue = issue.toDto()
            )
            if (response.isSuccessful) {
                val body = response.body() ?: return Failure(SerializationError())
                Success(body.toDomain())
            } else {
                val errorBody = response.errorBody() ?: return Failure(
                    NetworkErrorMapper.mapError(
                        response.code()
                    )
                )
                try {
                    val errorDto = json.decodeFromString<GitHubErrorDto>(errorBody.string())
                    Failure(InvalidRequest(errorDto.message))
                } catch (_: SerializationException) {
                    Failure(
                        NetworkErrorMapper.mapError(response.code())
                    )
                }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (_: IOException) {
            Failure(UnknownOperationOutcome)
        } catch (_: SerializationException) {
            Failure(SerializationError())
        } catch (_: Exception) {
            Failure(Unknown)
        }


    }
}