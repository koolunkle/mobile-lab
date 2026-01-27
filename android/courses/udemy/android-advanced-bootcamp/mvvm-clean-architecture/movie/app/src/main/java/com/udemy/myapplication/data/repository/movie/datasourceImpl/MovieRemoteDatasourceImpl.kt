package com.udemy.myapplication.data.repository.movie.datasourceImpl

import com.udemy.myapplication.data.api.TMDBService
import com.udemy.myapplication.data.model.movie.MovieList
import com.udemy.myapplication.data.repository.movie.datasource.MovieRemoteDatasource
import retrofit2.Response

class MovieRemoteDatasourceImpl(private val tmdbService: TMDBService, private val apiKey: String) :
    MovieRemoteDatasource {

    override suspend fun getMovies(): Response<MovieList> = tmdbService.getPopularMovies(apiKey)
}