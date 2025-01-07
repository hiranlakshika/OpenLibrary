package com.restable.library.book.presentation.bool_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.restable.library.app.Route
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.book.domain.usecase.CheckWishlistStatusUseCase
import com.restable.library.book.domain.usecase.GetBookDescriptionUseCase
import com.restable.library.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
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
    private val checkWishlistStatusUseCase: CheckWishlistStatusUseCase,
    private val getBookDescriptionUseCase: GetBookDescriptionUseCase,
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id
    private var updateWishlistStatusJob: Job? = null

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

    private fun updateWishlistStatus() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = checkWishlistStatusUseCase(bookId)) {
            is Result.Error -> {
                _state.update { it.copy(isLoading = false, error = result.error.name) }
            }

            is Result.Success -> {
                updateWishlistStatusJob?.cancel()
                updateWishlistStatusJob = result.data.onEach { isLocal ->
                    _state.update {
                        it.copy(
                            isLocal = isLocal
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun fetchBookDescription() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getBookDescriptionUseCase(bookId)) {
            is Result.Error -> {
                _state.update { it.copy(isLoading = false, error = result.error.name) }
            }

            is Result.Success -> {
                _state.update {
                    it.copy(
                        book = it.book?.copy(description = result.data)
                    )
                }
            }
        }
    }
}