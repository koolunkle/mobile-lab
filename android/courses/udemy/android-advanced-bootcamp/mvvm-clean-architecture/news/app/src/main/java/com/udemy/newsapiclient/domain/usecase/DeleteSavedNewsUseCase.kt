package com.udemy.newsapiclient.domain.usecase

import com.udemy.newsapiclient.data.model.Article
import com.udemy.newsapiclient.domain.repository.NewsRepository

class DeleteSavedNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(article: Article) = newsRepository.deleteNews(article)
}