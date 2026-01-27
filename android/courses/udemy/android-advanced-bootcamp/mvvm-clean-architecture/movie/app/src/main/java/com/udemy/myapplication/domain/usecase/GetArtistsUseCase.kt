package com.udemy.myapplication.domain.usecase

import com.udemy.myapplication.data.model.artist.Artist
import com.udemy.myapplication.domain.repository.ArtistRepository

class GetArtistsUseCase(private val artistRepository: ArtistRepository) {

    suspend fun execute(): List<Artist>? = artistRepository.getArtists()
}