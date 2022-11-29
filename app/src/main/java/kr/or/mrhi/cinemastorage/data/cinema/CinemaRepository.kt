package kr.or.mrhi.cinemastorage.data.cinema

import kr.or.mrhi.cinemastorage.network.CinemaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CinemaRepository {

    private val cinemaApi: CinemaAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        cinemaApi = retrofit.create(CinemaAPI::class.java)
    }

    fun getPopularCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getPopularCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun getTopRatedCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getTopRatedCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun getUpcomingCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getUpcomingCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}