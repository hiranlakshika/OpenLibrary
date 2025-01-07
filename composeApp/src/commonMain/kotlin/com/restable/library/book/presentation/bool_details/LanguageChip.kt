package com.restable.library.book.presentation.bool_details

import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.restable.library.core.presentation.Green100
import com.restable.library.core.presentation.GreyBlack

@Composable
fun LanguageChip(language: String) {
    SuggestionChip(
        onClick = { },
        label = { Text(language) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Green100,
            labelColor = GreyBlack
        )
    )
}