package com.udemy.newsapiclient.domain.usecase

import com.udemy.newsapiclient.data.model.Article
import com.udemy.newsapiclient.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(article: Article) = newsRepository.saveNews(article)
}