package com.udemy.myapplication.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udemy.myapplication.domain.usecase.GetMoviesUseCase
import com.udemy.myapplication.domain.usecase.UpdateMoviesUseCase

class MovieViewModelFactory(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val updateMoviesUseCase: UpdateMoviesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetMoviesUseCase::class.java, UpdateMoviesUseCase::class.java
        ).newInstance(getMoviesUseCase, updateMoviesUseCase)
    }
}