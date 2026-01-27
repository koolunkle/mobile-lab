package com.example.musicui

import androidx.annotation.DrawableRes

data class Library(
    @DrawableRes val icon: Int,
    val name: String,
)

val libraries = listOf(
    Library(icon = R.drawable.playlist_24, name = "Playlist"),
    Library(icon = R.drawable.microphone_24, name = "Artists"),
    Library(icon = R.drawable.album_24, name = "Album"),
    Library(icon = R.drawable.music_note_24, name = "Songs"),
    Library(icon = R.drawable.genre_24, name = "Genre"),
)