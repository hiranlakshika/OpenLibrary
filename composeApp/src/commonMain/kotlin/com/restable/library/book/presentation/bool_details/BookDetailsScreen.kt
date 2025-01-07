package com.restable.library.book.presentation.bool_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.restable.library.book.domain.model.Book
import com.restable.library.book.presentation.components.BookImage
import com.restable.library.core.presentation.Red200
import com.restable.library.core.presentation.Red500
import com.restable.library.core.utils.StringUtils.getRoundNumberText
import kotlin.math.round

@Composable
fun BookDetailScreen(viewModel: BookDetailViewModel, onBackPressed: () -> Unit) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    state.book?.let {
        BookDetailScreenCom(it, onBackPressed, state = state, onEvent = { event ->
            viewModel.onEvent(event)
        })
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreenCom(
    book: Book,
    onBackPressed: () -> Unit,
    state: BookDetailState? = null,
    onEvent: (BookDetailEvent) -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            title = {
                Text(
                    book.title, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier.shadow(elevation = 4.dp),
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
        )
    }, content = { padding ->
        Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())) {
            Text(
                text = book.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 12.dp).padding(start = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BookImage(imageUrl = book.imageUrl)
                    IconButton(
                        onClick = { onEvent(BookDetailEvent.OnWishlistClick) },
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Wishlist Button",
                            tint = if (state?.isLocal == true) Red500 else Red200
                        )
                    }
                    book.numPages?.let {
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Pages - ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600)
                            )
                            Text(
                                text = book.numPages.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    book.averageRating?.let { rating ->
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Rating - ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600)
                            )
                            Text(
                                text = getRoundNumberText(rating),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            if (book.languages.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .wrapContentSize(align = Alignment.Center).fillMaxWidth(),
                ) {
                    book.languages.forEach {
                        LanguageChip(it.uppercase())
                    }
                }
            }

            if (book.description?.isNotEmpty() == true) {
                Text(
                    text = book.description,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    textAlign = TextAlign.Justify
                )
            }
        }
    })
}