package com.restable.library.book.presentation.book_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.domain.model.Book
import com.restable.library.core.presentation.SandYellow
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
        BookListView(
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
private fun BookListView(state: BookListState, onEvent: (BookListEvent) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val wishListBooksListState = rememberLazyListState()

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = state.selectedTabIndex,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .widthIn(max = 700.dp)
                    .fillMaxWidth(),
                /*indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        color = SandYellow,
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[state.selectedTabIndex])
                    )
                }*/
            ) {
                Tab(
                    selected = state.selectedTabIndex == 0,
                    onClick = {
                        onEvent(BookListEvent.OnTabSelected(0))
                    },
                    modifier = Modifier.weight(1f),
                    selectedContentColor = SandYellow,
                    unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Available",
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                    )
                }
                Tab(
                    selected = state.selectedTabIndex == 1,
                    onClick = {
                        onEvent(BookListEvent.OnTabSelected(1))
                    },
                    modifier = Modifier.weight(1f),
                    selectedContentColor = SandYellow,
                    unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Wishlist",
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when(pageIndex) {
                        0 -> {
                            if(state.isLoading) {
                                CircularProgressIndicator()
                            } else {
                                when {
                                    state.error != null -> {
                                        Text(
                                            text = state.error,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.error
                                        )
                                    }
                                    state.bookList.isEmpty() -> {
                                        Text(
                                            text = "No results found",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.error
                                        )
                                    }
                                    else -> {
                                        BookList(
                                            books = state.bookList,
                                            onBookClick = {
                                                onEvent(BookListEvent.OnBookClick(it))
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                            scrollState = searchResultsListState
                                        )
                                    }
                                }
                            }
                        }
                        1 -> {
                            if(state.wishlistBooks.isEmpty()) {
                                Text(
                                    text = "Empty",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1,
                                )
                            } else {
                                BookList(
                                    books = state.wishlistBooks,
                                    onBookClick = {
                                        onEvent(BookListEvent.OnBookClick(it))
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                    scrollState = wishListBooksListState
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}