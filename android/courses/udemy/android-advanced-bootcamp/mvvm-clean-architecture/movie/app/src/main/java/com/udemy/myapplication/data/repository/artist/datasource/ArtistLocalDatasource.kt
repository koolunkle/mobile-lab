package com.udemy.myapplication.data.repository.artist.datasource

import com.udemy.myapplication.data.model.artist.Artist

interface ArtistLocalDatasource {

    suspend fun getArtistsFromDB(): List<Artist>
    suspend fun saveArtistsToDB(artists: List<Artist>)
    suspend fun clearAll()
}