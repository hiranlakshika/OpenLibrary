package com.restable.library.book.presentation.bool_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.restable.library.app.Route
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    private val _state = MutableStateFlow(BookDetailState())
    val state
        get() = _state.onStart {
            fetchBookDescription()
            updateWishlistStatus()
        }.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value
            )

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
                viewModelScope.launch {
                    if (state.value.isLocal) {
                        bookRepository.deleteFromWishlist(bookId)
                    } else {
                        state.value.book?.let { book ->
                            bookRepository.addToWishlist(book)
                        }
                    }
                }
            }
        }
    }

    private fun updateWishlistStatus() {
        bookRepository.isBookLocal(bookId).onEach { isLocal ->
                _state.update {
                    it.copy(
                        isLocal = isLocal
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository.getBookDescription(bookId).onSuccess { description ->
                    _state.update {
                        it.copy(
                            book = it.book?.copy(
                                description = description
                            ), isLoading = false
                        )
                    }
                }
        }
    }
}