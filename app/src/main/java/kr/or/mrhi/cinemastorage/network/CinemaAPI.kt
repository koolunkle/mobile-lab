package kr.or.mrhi.cinemastorage.network

import androidx.viewbinding.BuildConfig
import kr.or.mrhi.cinemastorage.data.cinema.GetCinemaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaAPI {

    @GET("movie/popular")
    fun getPopularCinema(
        @Query("api_key") apiKey: String = "${kr.or.mrhi.cinemastorage.BuildConfig.POPULAR_KEY}",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>

    @GET("movie/top_rated")
    fun getTopRatedCinema(
        @Query("api_key") apiKey: String = "${kr.or.mrhi.cinemastorage.BuildConfig.TOP_RATE_KEY}",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>

    @GET("movie/upcoming")
    fun getUpcomingCinema(
        @Query("api_key") apiKey: String = "${kr.or.mrhi.cinemastorage.BuildConfig.UPCOMING_KEY}",
        @Query("page") page: Int
    ): Call<GetCinemaResponse>
}