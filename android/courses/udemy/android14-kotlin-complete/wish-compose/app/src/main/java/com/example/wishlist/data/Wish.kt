package com.example.wishlist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wish-table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "wish-title")
    val title: String = "",
    @ColumnInfo(name = "wish-desc")
    val description: String = "",
)

object DummyWish {
    val wishList = listOf(
        Wish(1, "iPhone 15", "iPhone 15 Pro Max"),
        Wish(2, "MacBook Air", "MacBook Air M3"),
        Wish(3, "iPad Air", "iPad Air M2"),
    )
}
