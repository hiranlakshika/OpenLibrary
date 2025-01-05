package com.restable.library.book.domain.usecase

import com.restable.library.book.domain.model.Book
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.error.LibraryException

class SearchBooksUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(query: String): Result<List<Book>, DataError.NetworkError> {
        return try {
            bookRepository.searchBooks(query)
        } catch (exception: LibraryException) {
            Result.Error(error = (exception.error as DataError.NetworkError))
        }
    }
}