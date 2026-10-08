package com.karthik.pro.engr.github.api.android_interview_mastery.domain.usecase

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.Repo
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.repository.GithubApiRepository
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.result.AppResult
import java.util.UUID
import javax.inject.Inject

class GetReposListUseCase @Inject constructor(
    private val githubApiRepository: GithubApiRepository
) {

    suspend operator fun invoke(
        username: String,
        sort: String,
        order: String,
        header: String
    ): AppResult<List<Repo>> {

        return githubApiRepository.getRepos(
            username = username,
            sort = sort,
            order = order,
            header = header
        )
    }
}