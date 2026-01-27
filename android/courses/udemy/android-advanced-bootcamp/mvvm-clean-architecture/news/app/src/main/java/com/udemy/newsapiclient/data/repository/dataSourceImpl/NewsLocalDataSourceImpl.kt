package com.udemy.newsapiclient.data.repository.dataSourceImpl

import com.udemy.newsapiclient.data.db.ArticleDAO
import com.udemy.newsapiclient.data.model.Article
import com.udemy.newsapiclient.data.repository.dataSource.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImpl(
    private val articleDAO: ArticleDAO
) : NewsLocalDataSource {

    override suspend fun saveArticleToDB(article: Article) {
        articleDAO.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun deleteArticleToDB(article: Article) {
        articleDAO.deleteArticle(article)
    }
}