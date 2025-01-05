package com.restable.library.book.presentation.bool_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.domain.model.Book
import com.restable.library.book.presentation.components.BookImage

@Composable
fun BookDetailScreen(viewModel: BookDetailViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    state.book?.let { BookDetailScreenCom(it) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreenCom(book: Book) {
    Column {
        Text(text = book.title, textAlign = TextAlign.Center)
        BookImage(imageUrl = book.imageUrl)
        if (book.languages.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            ) {
                book.languages.forEach {
                    LanguageChip(it)
                }
            }
        }
    }
}