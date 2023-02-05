package kr.or.mrhi.cinemastorage.network

import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.data.cinema.GetCinemaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaAPI {
    @GET("movie/popular")
    fun getPopularCinema(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>

    @GET("movie/top_rated")
    fun getTopRatedCinema(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>

    @GET("movie/upcoming")
    fun getUpcomingCinema(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>
}