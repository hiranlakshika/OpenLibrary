package com.restable.library.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restable.library.book.domain.usecase.GetWishListUseCase
import com.restable.library.book.domain.usecase.SearchBooksUseCase
import com.restable.library.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val getWishListUseCase: GetWishListUseCase,
) : ViewModel() {
    private var observeWishlistJob: Job? = null
    private val _state = MutableStateFlow(BookListState())
    val state = _state.onStart {
        fetchLocalBooks()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value
    )

    fun onEvent(event: BookListEvent) {
        when (event) {
            BookListEvent.OnClickSearch -> {
                _state.update { it.copy(error = null, isLoading = true) }
            }

            BookListEvent.OnSearchError -> {
                _state.update { it.copy(error = null, isLoading = false) }
            }

            is BookListEvent.OnTabSelected -> {
                _state.update { it.copy(selectedTabIndex = event.index) }
            }

            is BookListEvent.OnBookClick -> {}
            is BookListEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = event.query)
                }
                getBooks(event.query)
            }
        }
    }

    private fun getBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(isLoading = true) }
        delay(5000L)
        when (val result = searchBooksUseCase(query)) {
            is Result.Error -> {
                _state.update { it.copy(isLoading = false, error = result.error.name) }
            }

            is Result.Success -> {
                _state.update { it.copy(isLoading = false, bookList = result.data) }
            }
        }
    }

    private fun fetchLocalBooks() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getWishListUseCase()) {
            is Result.Error -> {
                _state.update { it.copy(isLoading = false, error = result.error.name) }
            }

            is Result.Success -> {
                observeWishlistJob?.cancel()
                observeWishlistJob = result.data.onEach { value ->
                    _state.update {
                        it.copy(isLoading = false, wishlistBooks = value)
                    }

                }.launchIn(viewModelScope)
            }
        }
    }
}