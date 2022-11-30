package kr.or.mrhi.cinemastorage.data

data class User(
    val userKey: String,
    val nickname: String? = "",
    val password: String? = "",
    val profileImageUrl: String? = ""
)
