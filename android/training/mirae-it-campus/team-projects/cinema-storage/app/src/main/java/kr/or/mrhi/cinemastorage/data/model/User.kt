package kr.or.mrhi.cinemastorage.data.model

data class User(
    var key: String = "",
    var nickname: String? = "",
    val password: String? = ""
)