package com.udemy.myapplication.data.repository.artist.datasource

import com.udemy.myapplication.data.model.artist.Artist

interface ArtistCacheDatasource {

    suspend fun getArtistsFromCache(): List<Artist>
    suspend fun saveArtistsToCache(artists: List<Artist>)
}