package com.restable.library.book.presentation.bool_details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.presentation.components.BookImage

@Composable
fun BookDetailScreen(viewModel: BookDetailViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        Text(text = state.book?.title ?: "", textAlign = TextAlign.Center)
        BookImage(imageUrl = state.book?.imageUrl)
    }
}