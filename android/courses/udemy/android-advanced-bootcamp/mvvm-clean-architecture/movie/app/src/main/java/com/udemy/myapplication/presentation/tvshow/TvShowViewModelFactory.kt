package com.udemy.myapplication.presentation.tvshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udemy.myapplication.domain.usecase.GetTvShowsUseCase
import com.udemy.myapplication.domain.usecase.UpdateTvShowsUseCase

class TvShowViewModelFactory(
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val updateTvShowsUseCase: UpdateTvShowsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetTvShowsUseCase::class.java, UpdateTvShowsUseCase::class.java
        ).newInstance(getTvShowsUseCase, updateTvShowsUseCase)
    }
}