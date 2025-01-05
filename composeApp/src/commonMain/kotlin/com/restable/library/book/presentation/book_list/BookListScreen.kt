package com.restable.library.book.presentation.book_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.domain.model.Book
import com.restable.library.book.presentation.components.SearchBar
import com.restable.library.core.presentation.BluePin
import com.restable.library.core.presentation.components.DownloadingAnimation
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(viewModel: BookListViewModel = koinViewModel(), onBookClick: (Book) -> Unit) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Open Library",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
            )
        },
    ) { innerPadding ->
        BookListView(
            state,
            onEvent = { event ->
                when (event) {
                    is BookListEvent.OnBookClick -> onBookClick(event.book)
                    else -> Unit
                }
                viewModel.onEvent(event)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun BookListView(
    state: BookListState,
    onEvent: (BookListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

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

    Column(modifier = modifier) {
        SearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { onEvent(BookListEvent.OnSearchQueryChange(it)) },
            modifier = Modifier.padding(top = 12.dp)
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
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        color = BluePin,
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[state.selectedTabIndex])
                    )
                }
            ) {
                Tab(
                    selected = state.selectedTabIndex == 0,
                    onClick = {
                        onEvent(BookListEvent.OnTabSelected(0))
                    },
                    modifier = Modifier.weight(1f),
                    selectedContentColor = BluePin,
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
                    selectedContentColor = BluePin,
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
                    when (pageIndex) {
                        0 -> {
                            if (state.isLoading) {
                                DownloadingAnimation(
                                    modifier = Modifier.height(48.dp).width(48.dp)
                                )
                            } else {
                                when {
                                    state.error != null -> {
                                        Text(
                                            text = state.error,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    state.bookList.isEmpty() -> {
                                        Text(
                                            text = "No results found",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error
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
                            if (state.wishlistBooks.isEmpty()) {
                                Text(
                                    text = "Empty",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
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