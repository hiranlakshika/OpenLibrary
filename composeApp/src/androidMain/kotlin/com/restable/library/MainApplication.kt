package com.restable.library

import android.app.Application
import com.restable.library.book.data.di.bookCommonModule
import com.restable.library.core.di.coreCommonModule
import com.restable.library.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin{
            androidContext(this@MainApplication)
        }
    }
}