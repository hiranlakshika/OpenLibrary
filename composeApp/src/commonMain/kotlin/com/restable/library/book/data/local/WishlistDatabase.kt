package com.restable.library.book.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BookEntity::class],
    version = 1
)

@TypeConverters(
    StringListTypeConverter::class
)

@ConstructedBy(BookLocalDatabaseConstructor::class)
abstract class WishlistDatabase : RoomDatabase() {
    abstract val localBookDao: LocalBookDao

    companion object {
        const val DB_NAME = "book.db"
    }
}