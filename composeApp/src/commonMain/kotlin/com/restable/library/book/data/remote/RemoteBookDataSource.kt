package com.restable.library.book.data.remote

import com.restable.library.book.data.remote.dto.BookWorkDto
import com.restable.library.book.data.remote.dto.SearchResponseDto
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.NetworkError>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.NetworkError>
}