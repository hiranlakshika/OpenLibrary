package com.restable.library.core.di

import com.restable.library.book.data.di.bookCommonModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModule, coreCommonModule, bookCommonModule)
    }
}