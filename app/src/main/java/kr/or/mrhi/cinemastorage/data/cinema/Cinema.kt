package kr.or.mrhi.cinemastorage.data.cinema

import com.google.gson.annotations.SerializedName

data class Cinema(
    @SerializedName("id") val id: Long,

    @SerializedName("title") val title: String,

    @SerializedName("overview") val overview: String,

    @SerializedName("poster_path") val posterPath: String
)