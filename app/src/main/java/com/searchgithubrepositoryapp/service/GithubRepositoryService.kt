package com.searchgithubrepositoryapp.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepositoryService {
    @GET("search/repositories")
    fun fetchGithubRepository(
        @Query("q")
        q: String
    ): Call<GithubRepositoryNameInfo>
}