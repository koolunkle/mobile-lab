package com.udemy.newsapiclient.domain.usecase

import com.udemy.newsapiclient.data.model.Article
import com.udemy.newsapiclient.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {

    fun execute(): Flow<List<Article>> {
        return newsRepository.getSavedNews()
    }
}