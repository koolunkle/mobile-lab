package com.udemy.myapplication.data.repository.tvshow.datasource

import com.udemy.myapplication.data.model.tvshow.TvShow

interface TvShowCacheDatasource {

    suspend fun getTvShowsFromCache(): List<TvShow>
    suspend fun saveTvShowsToCache(tvShows: List<TvShow>)
}