package com.udemy.myapplication.presentation.di.core

import com.udemy.myapplication.data.repository.artist.datasource.ArtistCacheDatasource
import com.udemy.myapplication.data.repository.artist.datasourceImpl.ArtistCacheDatasourceImpl
import com.udemy.myapplication.data.repository.movie.datasource.MovieCacheDatasource
import com.udemy.myapplication.data.repository.movie.datasourceImpl.MovieCacheDatasourceImpl
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowCacheDatasource
import com.udemy.myapplication.data.repository.tvshow.datasourceImpl.TvShowCacheDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheDataModule {

    @Singleton
    @Provides
    fun provideMovieCacheDatasource(): MovieCacheDatasource {
        return MovieCacheDatasourceImpl()
    }

    @Singleton
    @Provides
    fun provideTvShowCacheDatasource(): TvShowCacheDatasource {
        return TvShowCacheDatasourceImpl()
    }

    @Singleton
    @Provides
    fun provideArtistCacheDatasource(): ArtistCacheDatasource {
        return ArtistCacheDatasourceImpl()
    }
}