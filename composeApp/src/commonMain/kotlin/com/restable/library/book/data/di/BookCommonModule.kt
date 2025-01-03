package com.restable.library.book.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.restable.library.book.data.local.DatabaseFactory
import com.restable.library.book.data.local.WishlistDatabase
import com.restable.library.book.data.remote.RemoteBookDataSource
import com.restable.library.book.data.remote.RemoteBookDataSourceImpl
import com.restable.library.book.data.repository.BookRepositoryImpl
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.book.domain.usecase.AddToWishlistUseCase
import com.restable.library.book.domain.usecase.SearchBooksUseCase
import com.restable.library.book.presentation.book_list.BookListViewModel
import com.restable.library.book.presentation.bool_details.BookDetailViewModel
import com.restable.library.core.data.HttpClientFactory
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val bookCommonModule = module {
    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    single { AddToWishlistUseCase(get()) }
    single { SearchBooksUseCase(get()) }
    single<BookRepository> { BookRepositoryImpl() }
    single<RemoteBookDataSource> { RemoteBookDataSourceImpl(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<WishlistDatabase>().localBookDao }
    single { HttpClientFactory.create(get()) }
}