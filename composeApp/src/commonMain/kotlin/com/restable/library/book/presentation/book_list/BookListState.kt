package com.restable.library.book.presentation.book_list

import com.restable.library.book.domain.model.Book

data class BookListState(
    val searchQuery: String = "",
    val bookList: List<Book> = emptyList(),
    val wishlistBooks: List<Book> = emptyList(),
    val selectedTabIndex: Int = 0,
    val error: String? = null,
    val isLoading: Boolean = false
)