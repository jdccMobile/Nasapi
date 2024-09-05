package com.jdccmobile.nasapi

import android.app.Application
import androidx.room.Room
import com.jdccmobile.data.local.AstronomicEventDatabase
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.RetrofitServiceFactory
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.data.repository.AstronomicEventRepositoryImpl
import com.jdccmobile.data.repository.RequestAndInsertEventsPerWeek
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.domain.usecase.GetAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.features.favorites.FavoritesViewModel
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
    viewModelOf(::FavoritesViewModel)
}

private val dataModule = module {
    factoryOf(::AstronomicEventRepositoryImpl) bind AstronomicEventRepository::class
    factoryOf(::AstronomicEventLocalDataSource)
    factoryOf(::RequestAndInsertEventsPerWeek)

    single<RetrofitService> { RetrofitServiceFactory.makeRetrofitService() }
    factory<AstronomicEventRemoteDataSource> {
        AstronomicEventRemoteDataSource(
            get(named("apiKey")),
            get(),
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AstronomicEventDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single { get<AstronomicEventDatabase>().getAstronomicEventDao() }
}

private val domainModule = module {
    factoryOf(::GetAstronomicEventsUseCase)
}

private const val API_KEY_NAMED = "apiKey"
private const val DATABASE_NAME = "favorites_database"
