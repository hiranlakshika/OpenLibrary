package com.restable.library.book.data.repository

import com.restable.library.book.domain.model.Book
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import kotlinx.coroutines.flow.Flow

class BookRepositoryImpl : BookRepository {
    override suspend fun searchBooks(query: String): Flow<List<Book>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        TODO("Not yet implemented")
    }

    override fun getLocalBooks(): Flow<List<Book>> {
        TODO("Not yet implemented")
    }

    override fun isBookLocal(id: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addToWishlist(book: Book): DataError.LocalDataError {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromWishlist(id: String) {
        TODO("Not yet implemented")
    }
}