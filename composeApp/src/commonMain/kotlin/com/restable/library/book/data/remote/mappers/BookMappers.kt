package com.restable.library.book.data.remote.mappers

import com.restable.library.book.data.local.BookEntity
import com.restable.library.book.data.remote.dto.SearchedBookDto
import com.restable.library.book.domain.model.Book


fun SearchedBookDto.toBook(): Book = Book(
    id = id.substringAfterLast("/"),
    title = title,
    imageUrl = if (coverKey != null) {
        "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
    } else {
        "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
    },
    authors = authorNames ?: emptyList(),
    description = null,
    languages = languages ?: emptyList(),
    firstPublishYear = firstPublishYear.toString(),
    averageRating = ratingsAverage,
    ratingCount = ratingsCount,
    numPages = numPagesMedian,
    numEditions = numEditions ?: 0
)

fun Book.toBookEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    languages = languages,
    authors = authors,
    firstPublishYear = firstPublishYear,
    ratingsAverage = averageRating,
    ratingsCount = ratingCount,
    numPagesMedian = numPages,
    numEditions = numEditions
)

fun BookEntity.toBook(): Book = Book(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    languages = languages,
    authors = authors,
    firstPublishYear = firstPublishYear,
    averageRating = ratingsAverage,
    ratingCount = ratingsCount,
    numPages = numPagesMedian,
    numEditions = numEditions
)