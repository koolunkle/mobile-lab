package com.udemy.myapplication.presentation.di.core

import com.udemy.myapplication.data.repository.artist.ArtistRepositoryImpl
import com.udemy.myapplication.data.repository.artist.datasource.ArtistCacheDatasource
import com.udemy.myapplication.data.repository.artist.datasource.ArtistLocalDatasource
import com.udemy.myapplication.data.repository.artist.datasource.ArtistRemoteDatasource
import com.udemy.myapplication.data.repository.movie.MovieRepositoryImpl
import com.udemy.myapplication.data.repository.movie.datasource.MovieCacheDatasource
import com.udemy.myapplication.data.repository.movie.datasource.MovieLocalDatasource
import com.udemy.myapplication.data.repository.movie.datasource.MovieRemoteDatasource
import com.udemy.myapplication.data.repository.tvshow.TvShowRepositoryImpl
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowCacheDatasource
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowLocalDatasource
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowRemoteDatasource
import com.udemy.myapplication.domain.repository.ArtistRepository
import com.udemy.myapplication.domain.repository.MovieRepository
import com.udemy.myapplication.domain.repository.TvShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieRemoteDatasource: MovieRemoteDatasource,
        movieLocalDatasource: MovieLocalDatasource,
        movieCacheDatasource: MovieCacheDatasource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            movieRemoteDatasource, movieLocalDatasource, movieCacheDatasource
        )
    }

    @Singleton
    @Provides
    fun provideTvShowRepository(
        tvShowRemoteDatasource: TvShowRemoteDatasource,
        tvShowLocalDatasource: TvShowLocalDatasource,
        tvShowCacheDatasource: TvShowCacheDatasource,
    ): TvShowRepository {
        return TvShowRepositoryImpl(
            tvShowRemoteDatasource, tvShowLocalDatasource, tvShowCacheDatasource
        )
    }

    @Singleton
    @Provides
    fun provideArtistRepository(
        artistRemoteDatasource: ArtistRemoteDatasource,
        artistLocalDatasource: ArtistLocalDatasource,
        artistCacheDatasource: ArtistCacheDatasource,
    ): ArtistRepository {
        return ArtistRepositoryImpl(
            artistRemoteDatasource, artistLocalDatasource, artistCacheDatasource
        )
    }
}