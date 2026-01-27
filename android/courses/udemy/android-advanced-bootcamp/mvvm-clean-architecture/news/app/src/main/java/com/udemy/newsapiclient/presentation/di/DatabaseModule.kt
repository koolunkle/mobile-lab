package com.udemy.newsapiclient.presentation.di

import android.app.Application
import androidx.room.Room
import com.udemy.newsapiclient.data.db.ArticleDAO
import com.udemy.newsapiclient.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providesNewsDatabase(application: Application): ArticleDatabase {
        return Room.databaseBuilder(application, ArticleDatabase::class.java, "news_db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesNewsDao(articleDatabase: ArticleDatabase): ArticleDAO {
        return articleDatabase.getArticleDAO()
    }
}