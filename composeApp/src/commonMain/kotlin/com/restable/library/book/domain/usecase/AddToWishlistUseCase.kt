package com.restable.library.book.domain.usecase

import com.restable.library.book.domain.model.Book
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.EmptyResult
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.error.LibraryException

class AddToWishlistUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(book: Book): EmptyResult<DataError.LocalDataError> = try {
        bookRepository.addToWishlist(book)
    } catch (exception: LibraryException) {
        Result.Error(error = (exception.error as DataError.LocalDataError))
    }
}