package com.restable.library.book.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.restable.library.book.data.local.DatabaseFactory
import com.restable.library.book.data.local.WishlistDatabase
import com.restable.library.book.data.remote.RemoteBookDataSource
import com.restable.library.book.data.remote.RemoteBookDataSourceImpl
import com.restable.library.book.data.repository.BookRepositoryImpl
import com.restable.library.book.domain.repository.BookRepository
import com.restable.library.book.domain.usecase.AddToWishlistUseCase
import com.restable.library.book.domain.usecase.GetBookDescriptionUseCase
import com.restable.library.book.domain.usecase.GetWishListUseCase
import com.restable.library.book.domain.usecase.SearchBooksUseCase
import com.restable.library.book.presentation.book_list.BookListViewModel
import com.restable.library.book.presentation.bool_details.BookDetailViewModel
import com.restable.library.book.presentation.selected_book.SelectedBookViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bookCommonModule = module {
    single { AddToWishlistUseCase(get()) }
    single { SearchBooksUseCase(get()) }
    single { GetBookDescriptionUseCase(get()) }
    single { GetWishListUseCase(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<WishlistDatabase>().localBookDao }
    singleOf(::RemoteBookDataSourceImpl).bind<RemoteBookDataSource>()
    singleOf(::BookRepositoryImpl).bind<BookRepository>()
    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}