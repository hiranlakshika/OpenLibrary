package com.restable.library.book.presentation.book_list

import com.restable.library.book.domain.model.Book

data class BookListState(
    val surveyList: List<Book> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)