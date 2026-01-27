package com.udemy.myapplication.domain.usecase

import com.udemy.myapplication.data.model.movie.Movie
import com.udemy.myapplication.domain.repository.MovieRepository

class UpdateMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(): List<Movie>? = movieRepository.updateMovies()
}