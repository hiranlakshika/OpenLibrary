package com.restable.library.book.domain.repository

import com.restable.library.book.domain.model.Book
import com.restable.library.core.domain.error.DataError
import kotlinx.coroutines.flow.Flow
import com.restable.library.core.domain.Result

interface BookRepository {
    suspend fun searchBooks(query: String): Flow<List<Book>>
    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    fun getLocalBooks(): Flow<List<Book>>
    fun isBookLocal(id: String): Flow<Boolean>

    suspend fun addToWishlist(book: Book): DataError.LocalDataError
    suspend fun deleteFromWishlist(id: String)
}