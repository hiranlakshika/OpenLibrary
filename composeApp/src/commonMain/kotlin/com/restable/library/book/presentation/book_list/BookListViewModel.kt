package com.restable.library.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restable.library.book.domain.usecase.SearchBooksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.restable.library.core.domain.Result
import kotlinx.coroutines.flow.stateIn

class BookListViewModel(private val searchBooksUseCase: SearchBooksUseCase) : ViewModel() {

    private val _state = MutableStateFlow(BookListState())
    val state get() = _state

    fun onEvent(event: BookListEvent) {
        when (event) {
            BookListEvent.OnClickSearch -> {
                _state.update { it.copy(error = null, isLoading = true) }
            }

            BookListEvent.OnSearchError -> {
                _state.update { it.copy(error = null, isLoading = false) }
            }
        }
    }

    private fun getBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(isLoading = true) }
        when (val result = searchBooksUseCase(query)) {
            is Result.Error -> {
                _state.update { it.copy(isLoading = false, error = result.error.name) }
            }

            is Result.Success -> {
                result.data.onEach { data ->
                    _state.update { it.copy(isLoading = false, surveyList = data) }
                }.stateIn(this)
            }
        }
    }
}