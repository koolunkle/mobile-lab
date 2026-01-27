package com.udemy.myapplication.data.repository.artist.datasourceImpl

import com.udemy.myapplication.data.api.TMDBService
import com.udemy.myapplication.data.model.artist.ArtistList
import com.udemy.myapplication.data.repository.artist.datasource.ArtistRemoteDatasource
import retrofit2.Response

class ArtistRemoteDatasourceImpl(private val tmdbService: TMDBService, private val apiKey: String) :
    ArtistRemoteDatasource {

    override suspend fun getArtists(): Response<ArtistList> = tmdbService.getPopularArtists(apiKey)
}