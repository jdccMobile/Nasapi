package com.jdccmobile.nasapi

import android.app.Application
import com.jdccmobile.nasapi.ui.features.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

fun Application.initDi() {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDi)
        modules(appModule, dataModule, domainModule)
    }
}

private val appModule = module {
    viewModelOf(::HomeViewModel)
}

private val dataModule = module {
}

private val domainModule = module {
}
