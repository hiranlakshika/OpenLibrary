package com.restable.library.book.presentation.bool_details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BookDetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state get() = _state

    fun onEvent(event: BookDetailEvent) {

    }
}