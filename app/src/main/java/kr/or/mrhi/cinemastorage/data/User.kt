package kr.or.mrhi.cinemastorage.data

data class User(
    var key: String = "",
    var nickname: String? = "",
    val password: String? = ""
)