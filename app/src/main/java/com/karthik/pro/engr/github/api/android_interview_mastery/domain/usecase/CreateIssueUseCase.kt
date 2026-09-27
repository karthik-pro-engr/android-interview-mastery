package com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.CreateIssue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Issue
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import java.util.UUID

class CreateIssueUseCase(
    private val githubApiRepository: GithubApiRepository
) {

    suspend operator fun invoke(
        owner: String,
        repo: String,
        issue: CreateIssue
    ): AppResult<Issue> {
        val idempotencyKey = UUID.randomUUID().toString()
        return githubApiRepository.createIssue(
            owner = owner,
            repo = repo,
            idempotencyKey = idempotencyKey,
            issue = issue
        )
    }
}