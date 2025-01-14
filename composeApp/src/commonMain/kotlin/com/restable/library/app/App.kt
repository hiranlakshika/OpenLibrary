package com.restable.library.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.restable.library.book.presentation.selected_book.SelectedBookViewModel
import com.restable.library.book.presentation.book_list.BookListScreen
import com.restable.library.book.presentation.book_list.BookListViewModel
import com.restable.library.book.presentation.bool_details.BookDetailEvent
import com.restable.library.book.presentation.bool_details.BookDetailScreen
import com.restable.library.book.presentation.bool_details.BookDetailViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController, startDestination = Route.BookGraph
        ) {
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {
                composable<Route.BookList>(exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }) {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel =
                        it.bookCommonModule<SelectedBookViewModel>(navController)

                    LaunchedEffect(true) {
                        selectedBookViewModel.onSelectBook(null)
                    }

                    BookListScreen(viewModel, onBookClick = { book ->
                        selectedBookViewModel.onSelectBook(book)
                        navController.navigate(
                            Route.BookDetail(book.id)
                        )
                    })
                }
                composable<Route.BookDetail>(enterTransition = {
                    slideInHorizontally { initialOffset ->
                        initialOffset
                    }
                }, exitTransition = {
                    slideOutHorizontally { initialOffset ->
                        initialOffset
                    }
                }) { it ->
                    val selectedBookViewModel =
                        it.bookCommonModule<SelectedBookViewModel>(navController)
                    val viewModel = koinViewModel<BookDetailViewModel>()

                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedBook) {
                        selectedBook?.let {
                            viewModel.onEvent(BookDetailEvent.OnSelectedBookChange(it))
                        }
                    }

                    BookDetailScreen(viewModel, onBackPressed = {
                        navController.navigateUp()
                    })
                }

            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.bookCommonModule(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}