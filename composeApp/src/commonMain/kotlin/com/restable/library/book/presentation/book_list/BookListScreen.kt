package com.restable.library.book.presentation.book_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.domain.model.Book
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreen(viewModel: BookListViewModel = koinViewModel(), onBookClick: (Book) -> Unit) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Open Library") }
            )
        }
    ) {
        BookList(
            state,
            onEvent = { event ->
                when (event) {
                    is BookListEvent.OnBookClick -> onBookClick(event.book)
                    else -> Unit
                }
                viewModel.onEvent(event)
            },
        )
    }
}

@Composable
private fun BookList(state: BookListState, onEvent: (BookListEvent) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoriteBooksListState = rememberLazyListState()

    LaunchedEffect(state.bookList) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(BookListEvent.OnTabSelected(pagerState.currentPage))
    }

    Column(modifier = Modifier.padding(top = 12.dp)) {
        SearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { onEvent(BookListEvent.OnSearchQueryChange(it)) },
        )
    }

}