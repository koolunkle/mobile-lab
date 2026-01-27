package com.udemy.newsapiclient.data.repository.dataSource

import com.udemy.newsapiclient.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {

    suspend fun getTopHeadlines(country: String, page: Int): Response<APIResponse>

    suspend fun getSearchedNews(
        country: String, page: Int, searchQuery: String
    ): Response<APIResponse>
}