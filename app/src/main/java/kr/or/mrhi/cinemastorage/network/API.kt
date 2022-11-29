package kr.or.mrhi.cinemastorage.network

import kr.or.mrhi.cinemastorage.data.GetCinemaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("movie/popular")
    fun getPopularCinema(
        @Query("api_key") apiKey: String = "1a5e97ab7807cdecbc973e10e2ecf2ae",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>

    @GET("movie/top_rated")
    fun getTopRatedCinema(
        @Query("api_key") apiKey: String = "1a5e97ab7807cdecbc973e10e2ecf2ae",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>

    @GET("movie/upcoming")
    fun getUpcomingCinema(
        @Query("api_key") apiKey: String = "1a5e97ab7807cdecbc973e10e2ecf2ae",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>
}