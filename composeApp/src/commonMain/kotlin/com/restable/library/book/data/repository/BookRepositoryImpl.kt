package com.restable.library.book.data.repository

import androidx.sqlite.SQLiteException
import com.restable.library.book.data.local.LocalBookDao
import com.restable.library.book.data.remote.RemoteBookDataSource
import com.restable.library.book.data.remote.mappers.toBook
import com.restable.library.book.data.remote.mappers.toBookEntity
import com.restable.library.book.domain.model.Book
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.EmptyResult
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val localBookDao: LocalBookDao
) :
    BookRepository {
    override suspend fun searchBooks(query: String): Flow<List<Book>> {
        TODO("Not yet implemented")
        /*return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }*/
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        TODO("Not yet implemented")
    }

    override fun getLocalBooks(): Flow<List<Book>> = localBookDao
        .getWishlistBooks()
        .map { bookEntities ->
            bookEntities.map { it.toBook() }
        }

    override fun isBookLocal(id: String): Flow<Boolean> = localBookDao
        .getWishlistBooks()
        .map { bookEntities ->
            bookEntities.any { it.id == id }
        }

    override suspend fun addToWishlist(book: Book): EmptyResult<DataError.LocalDataError> = try {
        localBookDao.upsert(book.toBookEntity())
        Result.Success(Unit)
    } catch (e: SQLiteException) {
        Result.Error(DataError.LocalDataError.SOMETHING_WENT_WRONG)
    }

    override suspend fun deleteFromWishlist(id: String) = localBookDao.deleteWishlistBook(id)
}