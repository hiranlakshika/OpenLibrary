package com.restable.library.book.presentation.bool_details

import com.restable.library.book.domain.model.Book

data class BookDetailState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val book: Book? = null
)