package com.restable.library.book.data.local

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookLocalDatabaseConstructor: RoomDatabaseConstructor<WishlistDatabase> {
    override fun initialize(): WishlistDatabase
}