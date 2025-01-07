package com.restable.library.book.presentation.book_list

import com.restable.library.book.domain.model.Book

sealed interface BookListEvent {
    data object OnClickSearch : BookListEvent
    data object OnSearchError : BookListEvent
    data class OnSearchQueryChange(val query: String): BookListEvent
    data class OnBookClick(val book: Book) : BookListEvent
    data class OnTabSelected(val index: Int) : BookListEvent
}