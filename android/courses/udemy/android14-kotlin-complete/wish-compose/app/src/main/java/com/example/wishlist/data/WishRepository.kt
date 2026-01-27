package com.example.wishlist.data

import kotlinx.coroutines.flow.Flow

class WishRepository(private val wishDao: WishDao) {

    suspend fun addWish(wish: Wish) = wishDao.addWish(wish)

    fun getAllWishes(): Flow<List<Wish>> = wishDao.getAllWishes()

    suspend fun updateWish(wish: Wish) = wishDao.updateWish(wish)

    suspend fun deleteWish(wish: Wish) = wishDao.deleteWish(wish)

    fun getAllWishesById(id: Long): Flow<Wish> = wishDao.getAllWishesById(id)
}