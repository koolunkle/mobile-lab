package com.udemy.myapplication.presentation.di.core

import com.udemy.myapplication.BuildConfig
import com.udemy.myapplication.data.api.TMDBService
import com.udemy.myapplication.data.repository.artist.datasource.ArtistRemoteDatasource
import com.udemy.myapplication.data.repository.artist.datasourceImpl.ArtistRemoteDatasourceImpl
import com.udemy.myapplication.data.repository.movie.datasource.MovieRemoteDatasource
import com.udemy.myapplication.data.repository.movie.datasourceImpl.MovieRemoteDatasourceImpl
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowRemoteDatasource
import com.udemy.myapplication.data.repository.tvshow.datasourceImpl.TvShowRemoteDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule() {

    @Singleton
    @Provides
    fun provideMovieRemoteDatasource(tmdbService: TMDBService): MovieRemoteDatasource {
        return MovieRemoteDatasourceImpl(tmdbService, BuildConfig.API_KEY)
    }

    @Singleton
    @Provides
    fun provideTvShowRemoteDatasource(tmdbService: TMDBService): TvShowRemoteDatasource {
        return TvShowRemoteDatasourceImpl(tmdbService, BuildConfig.API_KEY)
    }

    @Singleton
    @Provides
    fun provideArtistRemoteDatasource(tmdbService: TMDBService): ArtistRemoteDatasource {
        return ArtistRemoteDatasourceImpl(tmdbService, BuildConfig.API_KEY)
    }
}