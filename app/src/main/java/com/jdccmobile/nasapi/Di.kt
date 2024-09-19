package com.jdccmobile.nasapi

import android.app.Application
import androidx.room.Room
import com.jdccmobile.data.local.AstronomicEventDatabase
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.local.datasource.AstronomicEventPhotoLocalDataSource
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.RetrofitServiceFactory
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.data.repository.AstronomicEventPhotoRepositoryImpl
import com.jdccmobile.data.repository.AstronomicEventRepositoryImpl
import com.jdccmobile.data.repository.RequestAndInsertEventsPerWeek
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.domain.usecase.eventPhoto.DeletePhotoUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetFavoriteAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetIfThereIsFavEventsUseCase
import com.jdccmobile.domain.usecase.eventPhoto.GetPhotosByEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.InsertPhotoUseCase
import com.jdccmobile.domain.usecase.events.RequestAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.features.details.DetailsViewModel
import com.jdccmobile.nasapi.ui.features.favorites.FavoritesViewModel
import com.jdccmobile.nasapi.ui.features.home.HomeScreenActions
import com.jdccmobile.nasapi.ui.features.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.viewmodel.dsl.viewModel
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

    viewModel { (screenActions: HomeScreenActions) ->
        HomeViewModel(
            screenActions = screenActions,
            requestAstronomicEventsUseCase = get(),
            getAstronomicEventsUseCase = get(),
            getIfThereIsFavEventsUseCase = get(),
        )
    }
    viewModelOf(::FavoritesViewModel)
    viewModel { (astronomicEventId: String) ->
        DetailsViewModel(
            astronomicEventId = astronomicEventId,
            switchEventFavoriteStatusUseCase = get(),
            getPhotosByEventUseCase = get(),
            insertPhotoUseCase = get(),
            deletePhotoUseCase = get(),
            getAstronomicEventUseCase = get(),
        )
    }
}

private val dataModule = module {
    factoryOf(::AstronomicEventRepositoryImpl) bind AstronomicEventRepository::class
    factoryOf(::AstronomicEventPhotoRepositoryImpl) bind AstronomicEventPhotoRepository::class
    factoryOf(::AstronomicEventLocalDataSource)
    factoryOf(::AstronomicEventPhotoLocalDataSource)
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
    single { get<AstronomicEventDatabase>().getAstronomicEventPhotoDao() }
}

private val domainModule = module {
    factoryOf(::RequestAstronomicEventsUseCase)
    factoryOf(::GetAstronomicEventUseCase)
    factoryOf(::GetAstronomicEventsUseCase)
    factoryOf(::GetFavoriteAstronomicEventsUseCase)
    factoryOf(::SwitchEventFavoriteStatusUseCase)
    factoryOf(::GetIfThereIsFavEventsUseCase)

    factoryOf(::GetPhotosByEventUseCase)
    factoryOf(::InsertPhotoUseCase)
    factoryOf(::DeletePhotoUseCase)
}

private const val API_KEY_NAMED = "apiKey"
private const val DATABASE_NAME = "favorites_database"
