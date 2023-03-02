package kr.or.mrhi.cinemastorage.data.model.cinema

import com.google.gson.annotations.SerializedName

data class Cinema(
    @SerializedName("id") val id: Long,

    @SerializedName("backdrop_path") val backdrop: String,

    @SerializedName("poster_path") val poster: String,

    @SerializedName("title") val title: String,

    @SerializedName("release_date") val release: String,

    @SerializedName("vote_average") val rating: Float,

    @SerializedName("overview") val overview: String
)