package com.udemy.newsapiclient.domain.usecase

import com.udemy.newsapiclient.data.model.APIResponse
import com.udemy.newsapiclient.data.util.Resource
import com.udemy.newsapiclient.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(country: String, page: Int, searchQuery: String): Resource<APIResponse> {
        return newsRepository.getSearchedNews(country, page, searchQuery)
    }
}