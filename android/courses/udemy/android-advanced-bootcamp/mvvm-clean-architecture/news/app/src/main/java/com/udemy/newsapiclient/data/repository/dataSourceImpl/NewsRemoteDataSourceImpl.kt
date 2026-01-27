package com.udemy.newsapiclient.data.repository.dataSourceImpl

import com.udemy.newsapiclient.data.api.NewsAPIService
import com.udemy.newsapiclient.data.model.APIResponse
import com.udemy.newsapiclient.data.repository.dataSource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService
) : NewsRemoteDataSource {

    override suspend fun getTopHeadlines(country: String, page: Int): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(country, page)
    }

    override suspend fun getSearchedNews(
        country: String, page: Int, searchQuery: String
    ): Response<APIResponse> {
        return newsAPIService.getSearchedTopHeadlines(country, page, searchQuery)
    }
}