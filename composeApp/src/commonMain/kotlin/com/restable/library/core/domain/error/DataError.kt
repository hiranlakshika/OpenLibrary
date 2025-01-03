package com.restable.library.core.domain.error

sealed interface DataError : Error {

    enum class UnknownError : DataError {
        SOMETHING_WENT_WRONG,
    }

    enum class SyncError : DataError {
        SYNC_FAILED,
        NO_CONNECTION
    }

    enum class LocalDataError : DataError {
        SOMETHING_WENT_WRONG,
        NOT_FOUND
    }

    enum class NetworkError : DataError {
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        REQUEST_FAILED,
        UNKNOWN
    }

    enum class ImageError : DataError {
        IMAGE_SYNC_FAILED,
        UNABLE_TO_GET_PENDING_IMAGE_UPLOADS
    }
}