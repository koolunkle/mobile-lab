package com.udemy.myapplication.domain.repository

import com.udemy.myapplication.data.model.artist.Artist

interface ArtistRepository {

    suspend fun getArtists(): List<Artist>?
    suspend fun updateArtists(): List<Artist>?
}