package com.restable.library.book.presentation.book_list

sealed interface BookListEvent {
    data object OnClickSearch: BookListEvent
    data object OnSearchError: BookListEvent
}