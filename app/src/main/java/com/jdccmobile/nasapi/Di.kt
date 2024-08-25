package com.jdccmobile.nasapi

import android.app.Application
import com.jdccmobile.data.remote.AstronomicEventRemoteDataSource
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.RetrofitServiceFactory
import com.jdccmobile.data.repository.AstronomicEventRepositoryImpl
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.nasapi.ui.features.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun Application.initDi() {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDi)
        modules(appModule, dataModule, domainModule)
    }
}

private val appModule = module {
    single(named(API_KEY_NAMED)) { androidApplication().getString(R.string.api_key) }

    viewModelOf(::HomeViewModel)
}

private val dataModule = module {
    single<RetrofitService> { RetrofitServiceFactory.makeRetrofitService() }
    factory<AstronomicEventRemoteDataSource> { AstronomicEventRemoteDataSource(get(named("apiKey")), get()) }
    factoryOf(::AstronomicEventRepositoryImpl) bind AstronomicEventRepository::class
}

private val domainModule = module {
}

private const val API_KEY_NAMED = "apiKey"
