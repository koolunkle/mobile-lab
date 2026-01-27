package com.udemy.myapplication.presentation.di.core

import com.udemy.myapplication.data.db.ArtistDao
import com.udemy.myapplication.data.db.MovieDao
import com.udemy.myapplication.data.db.TvShowDao
import com.udemy.myapplication.data.repository.artist.datasource.ArtistLocalDatasource
import com.udemy.myapplication.data.repository.artist.datasourceImpl.ArtistLocalDatasourceImpl
import com.udemy.myapplication.data.repository.movie.datasource.MovieLocalDatasource
import com.udemy.myapplication.data.repository.movie.datasourceImpl.MovieLocalDatasourceImpl
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowLocalDatasource
import com.udemy.myapplication.data.repository.tvshow.datasourceImpl.TvShowLocalDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideMovieLocalDatasource(movieDao: MovieDao): MovieLocalDatasource {
        return MovieLocalDatasourceImpl(movieDao)
    }

    @Singleton
    @Provides
    fun provideTvShowLocalDatasource(tvShowDao: TvShowDao): TvShowLocalDatasource {
        return TvShowLocalDatasourceImpl(tvShowDao)
    }

    @Singleton
    @Provides
    fun provideArtistLocalDatasource(artistDao: ArtistDao): ArtistLocalDatasource {
        return ArtistLocalDatasourceImpl(artistDao)
    }
}