package com.udemy.myapplication.data.repository.artist.datasource

import com.udemy.myapplication.data.model.artist.ArtistList
import retrofit2.Response

interface ArtistRemoteDatasource {

    suspend fun getArtists(): Response<ArtistList>
}