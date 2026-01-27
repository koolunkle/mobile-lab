package com.udemy.myapplication.data.repository.movie.datasource

import com.udemy.myapplication.data.model.movie.Movie

interface MovieLocalDatasource {

    suspend fun getMoviesFromDB(): List<Movie>
    suspend fun saveMoviesToDB(movies: List<Movie>)
    suspend fun clearAll()
}