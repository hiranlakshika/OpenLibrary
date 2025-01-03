package com.restable.library.book.data.util

import com.restable.library.core.domain.error.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext


suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.NetworkError> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return Result.Error(DataError.NetworkError.REQUEST_FAILED)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(DataError.NetworkError.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.NetworkError> = when (response.status.value) {
    in 200..299 -> {
        try {
            Result.Success(response.body<T>())
        } catch (e: NoTransformationFoundException) {
            Result.Error(DataError.NetworkError.SERIALIZATION)
        }
    }

    408 -> Result.Error(DataError.NetworkError.REQUEST_FAILED)
    429 -> Result.Error(DataError.NetworkError.REQUEST_FAILED)
    in 500..599 -> Result.Error(DataError.NetworkError.SERVER_ERROR)
    else -> Result.Error(DataError.NetworkError.UNKNOWN)
}