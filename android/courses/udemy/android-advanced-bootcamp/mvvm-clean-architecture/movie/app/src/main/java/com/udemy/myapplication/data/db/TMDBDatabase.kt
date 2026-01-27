package com.udemy.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udemy.myapplication.data.model.artist.Artist
import com.udemy.myapplication.data.model.movie.Movie
import com.udemy.myapplication.data.model.tvshow.TvShow

@Database(
    entities = [Movie::class, TvShow::class, Artist::class], version = 1, exportSchema = false
)
abstract class TMDBDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun tvShowsDao(): TvShowDao
    abstract fun artistDao(): ArtistDao
}