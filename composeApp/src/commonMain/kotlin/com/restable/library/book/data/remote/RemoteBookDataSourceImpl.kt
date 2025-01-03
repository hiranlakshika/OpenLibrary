package com.restable.library.book.data.remote

import io.ktor.client.HttpClient

class RemoteBookDataSourceImpl(private val httpClient: HttpClient) : RemoteBookDataSource {
}