package com.example.teachmint.repository

import com.example.teachmint.data.model.ApiResponse
import com.example.teachmint.data.model.Contributor
import com.example.teachmint.data.remote.RetrofitInstance
import retrofit2.Response

class RepositoryRepository() {
    suspend fun getRepositories(query: String): Response<ApiResponse> {
        return RetrofitInstance.api.searchRepositories(query)
    }

    suspend fun getContributors(ownerName: String, repoName: String): List<Contributor> {
        return RetrofitInstance.api.getContributors(ownerName = ownerName, repoName = repoName)
    }
}
