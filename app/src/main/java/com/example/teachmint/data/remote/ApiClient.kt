package com.example.teachmint.data.remote

import com.example.teachmint.data.model.ApiResponse
import com.example.teachmint.data.model.Contributor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): Response<ApiResponse>

    @GET("repos/{ownerName}/{repoName}/contributors")
    suspend fun getContributors(
        @Path("ownerName") ownerName: String,
        @Path("repoName") repoName: String,
    ): List<Contributor>
}
