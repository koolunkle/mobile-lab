package com.udemy.myapplication.domain.usecase

import com.udemy.myapplication.data.model.tvshow.TvShow
import com.udemy.myapplication.domain.repository.TvShowRepository

class UpdateTvShowsUseCase(private val tvShowRepository: TvShowRepository) {

    suspend fun execute(): List<TvShow>? = tvShowRepository.updateTvShows()
}