package com.udemy.myapplication.data.repository.movie.datasource

import com.udemy.myapplication.data.model.movie.MovieList
import retrofit2.Response

interface MovieRemoteDatasource {

    suspend fun getMovies(): Response<MovieList>
}