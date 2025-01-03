package com.restable.library.book.data.remote

import com.restable.library.book.data.remote.dto.BookWorkDto
import com.restable.library.book.data.remote.dto.SearchResponseDto
import com.restable.library.book.data.util.Constants
import com.restable.library.book.data.util.safeCall
import com.restable.library.core.domain.Result
import com.restable.library.core.domain.error.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteBookDataSourceImpl(private val httpClient: HttpClient) : RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.NetworkError> = safeCall<SearchResponseDto> {
        httpClient.get(
            urlString = "${Constants.BASE_URL}/search.json"
        ) {
            parameter("q", query)
            parameter("limit", resultLimit)
            parameter("language", "eng")
            parameter(
                "fields",
                "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count"
            )
        }
    }

    override suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.NetworkError> =
        safeCall<BookWorkDto> {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/works/$bookWorkId.json"
            )
        }
}