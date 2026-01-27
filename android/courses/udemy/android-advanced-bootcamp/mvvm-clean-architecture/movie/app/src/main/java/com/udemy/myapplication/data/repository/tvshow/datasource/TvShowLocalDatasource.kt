package com.udemy.myapplication.data.repository.tvshow.datasource

import com.udemy.myapplication.data.model.tvshow.TvShow

interface TvShowLocalDatasource {

    suspend fun getTvShowsFromDB(): List<TvShow>
    suspend fun saveTvShowsToDB(tvShows: List<TvShow>)
    suspend fun clearAll()
}