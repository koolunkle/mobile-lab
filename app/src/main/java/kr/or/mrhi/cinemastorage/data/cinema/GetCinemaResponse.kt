package kr.or.mrhi.cinemastorage.data.cinema

import com.google.gson.annotations.SerializedName

data class GetCinemaResponse(
    @SerializedName("page") val page: Int,

    @SerializedName("total_pages") val pages: Int,

    @SerializedName("results") val results: List<Cinema>
)