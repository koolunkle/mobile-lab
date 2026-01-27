package com.udemy.myapplication.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udemy.myapplication.domain.usecase.GetArtistsUseCase
import com.udemy.myapplication.domain.usecase.UpdateArtistsUseCase

class ArtistViewModelFactory(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val updateArtistsUseCase: UpdateArtistsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetArtistsUseCase::class.java, UpdateArtistsUseCase::class.java
        ).newInstance(getArtistsUseCase, updateArtistsUseCase)
    }
}