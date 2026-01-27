package com.udemy.myapplication.domain.repository

import com.udemy.myapplication.data.model.movie.Movie

interface MovieRepository {

    suspend fun getMovies(): List<Movie>?
    suspend fun updateMovies(): List<Movie>?
}