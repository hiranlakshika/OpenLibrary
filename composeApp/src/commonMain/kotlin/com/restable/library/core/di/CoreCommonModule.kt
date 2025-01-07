package com.restable.library.core.di

import com.restable.library.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val coreCommonModule = module { single { HttpClientFactory.create(get()) } }