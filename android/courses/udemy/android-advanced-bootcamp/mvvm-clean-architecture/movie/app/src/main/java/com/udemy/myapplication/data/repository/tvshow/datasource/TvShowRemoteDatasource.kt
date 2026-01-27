package com.udemy.myapplication.data.repository.tvshow.datasource

import com.udemy.myapplication.data.model.tvshow.TvShowList
import retrofit2.Response

interface TvShowRemoteDatasource {

    suspend fun getTvShows(): Response<TvShowList>
}