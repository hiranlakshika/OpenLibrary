package com.restable.library.core.di

import com.restable.library.core.data.HttpClientFactory
import org.koin.dsl.module

val coreCommonModule = module { single { HttpClientFactory.create(get()) } }