package com.restable.library.book.domain.usecase

import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import com.restable.library.core.domain.error.LibraryException
import kotlinx.coroutines.flow.Flow

class CheckWishlistStatusUseCase(private val bookRepository: BookRepository) {
    operator fun invoke(id: String): Result<Flow<Boolean>, DataError.LocalDataError> = try {
        val result = bookRepository.isBookLocal(id)
        Result.Success(data = result)
    } catch (exception: LibraryException) {
        Result.Error(error = (exception.error as DataError.LocalDataError))
    }
}