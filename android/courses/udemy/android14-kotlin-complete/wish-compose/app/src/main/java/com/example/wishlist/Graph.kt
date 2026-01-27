package com.example.wishlist

import android.content.Context
import androidx.room.Room
import com.example.wishlist.data.WishDatabase
import com.example.wishlist.data.WishRepository

object Graph {

    lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context = context,
            klass = WishDatabase::class.java,
            name = "wishlist.db",
        ).build()
    }
}