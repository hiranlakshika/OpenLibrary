package com.restable.library

import android.app.Application
import com.restable.library.book.data.di.bookCommonModule
import com.restable.library.core.di.coreCommonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(coreCommonModule, bookCommonModule)
        }
    }
}