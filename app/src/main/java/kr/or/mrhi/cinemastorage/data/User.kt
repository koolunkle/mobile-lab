package kr.or.mrhi.cinemastorage.data

data class User(
    val profileImage: Int,
    val nickname: String,
    val password: String,
    val review: Review,
)
