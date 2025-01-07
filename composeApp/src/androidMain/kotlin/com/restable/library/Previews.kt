package com.restable.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.restable.library.book.domain.model.Book
import com.restable.library.book.presentation.bool_details.BookDetailScreenCom

@Preview
@Composable
fun BookDetailScreenComPreview() {
    BookDetailScreenCom(
        Book(
            id = "1",
            title = "Test",
            imageUrl = "https://covers.openlibrary.org/a/olid/OL23919A-M.jpg",
            authors = mutableListOf("Sean"),
            description = "Java",
            languages = mutableListOf("English"),
            firstPublishYear = "",
            averageRating = 4.7,
            ratingCount = 100,
            numPages = 1200,
            numEditions = 3
        ),
        onBackPressed = {},
        onEvent = TODO()
    )
}