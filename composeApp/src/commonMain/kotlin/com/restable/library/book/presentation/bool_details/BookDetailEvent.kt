package com.restable.library.book.presentation.bool_details

import com.restable.library.book.domain.model.Book

sealed interface BookDetailEvent {
    data object OnWishlistClick: BookDetailEvent
    data class OnSelectedBookChange(val book: Book) : BookDetailEvent
}