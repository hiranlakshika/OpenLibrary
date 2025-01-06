package com.restable.library.book.domain.usecase

import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.error.LibraryException

class GetBookDescriptionUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(bookId: String): Result<String?, DataError> = try {
        bookRepository.getBookDescription(bookId)
    } catch (exception: LibraryException) {
        Result.Error(error = (exception.error as DataError))
    }
}