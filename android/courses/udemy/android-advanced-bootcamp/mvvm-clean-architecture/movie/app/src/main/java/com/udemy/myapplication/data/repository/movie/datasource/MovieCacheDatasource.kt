package com.udemy.myapplication.data.repository.movie.datasource

import com.udemy.myapplication.data.model.movie.Movie

interface MovieCacheDatasource {

    suspend fun getMoviesFromCache(): List<Movie>
    suspend fun saveMoviesToCache(movies: List<Movie>)
}