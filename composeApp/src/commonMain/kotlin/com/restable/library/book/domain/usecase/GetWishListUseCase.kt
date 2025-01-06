package com.restable.library.book.domain.usecase

import com.restable.library.book.domain.model.Book
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.error.LibraryException
import com.restable.library.core.domain.Result
import kotlinx.coroutines.flow.Flow

class GetWishListUseCase(private val bookRepository: BookRepository) {
    operator fun invoke(): Result<Flow<List<Book>>, DataError.LocalDataError> {
        return try {
            val result = bookRepository.getLocalBooks()
            Result.Success(data = result)
        } catch (exception: LibraryException) {
            Result.Error(error = (exception.error as DataError.LocalDataError))
        }
    }
}