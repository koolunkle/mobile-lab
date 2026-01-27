package com.udemy.myapplication.data.repository.tvshow.datasourceImpl

import com.udemy.myapplication.data.api.TMDBService
import com.udemy.myapplication.data.model.tvshow.TvShowList
import com.udemy.myapplication.data.repository.tvshow.datasource.TvShowRemoteDatasource
import retrofit2.Response

class TvShowRemoteDatasourceImpl(private val tmdbService: TMDBService, private val apiKey: String) :
    TvShowRemoteDatasource {

    override suspend fun getTvShows(): Response<TvShowList> = tmdbService.getPopularTvShows(apiKey)
}