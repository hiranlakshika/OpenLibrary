package com.restable.library.book.presentation.bool_details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class BookDetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state get() = _state

    fun onEvent(event: BookDetailEvent) {
        when (event) {
            is BookDetailEvent.OnSelectedBookChange -> {
                _state.update {
                    it.copy(
                        book = event.book
                    )
                }
            }

            is BookDetailEvent.OnWishlistClick -> {

            }
        }
    }
}