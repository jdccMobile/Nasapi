package com.jdccmobile.data.repository

import arrow.core.Either
import arrow.core.flatMap
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow

class AstronomicEventRepositoryImpl(
    private val localDataSource: AstronomicEventLocalDataSource,
    private val remoteDataSource: AstronomicEventRemoteDataSource,
    private val eventSyncManager: EventSyncManager,
) : AstronomicEventRepository {

    // todo get???
    override val astronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.astronomicEvents()

    override val favoriteAstronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.favoriteAstronomicEvents()

    override suspend fun requestAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> {
        return localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { Either.Left(it) },
            ifRight = { eventsInDb ->
                when (eventsInDb.size) {
                    EVENTS_IN_WEEK -> Either.Right(eventsInDb)
                    NO_EVENTS_IN_WEEK -> {
                        requestAndInsertEventsPerWeek(startDate, endDate).flatMap {
                            localDataSource.getAstronomicEventList(startDate, endDate)
                        }
                    }

                    else -> {
                        eventSyncManager.syncEvents(startDate, endDate).flatMap {
                            localDataSource.getAstronomicEventList(startDate, endDate)
                        }
                    }
                }
            },
        )
    }

    private suspend fun requestAndInsertEventsPerWeek(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> =
        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).fold(
            ifLeft = { Either.Left(it) },
            ifRight = { events ->
                localDataSource.insertAstronomicEventList(events)
            },
        )
}

private const val EVENTS_IN_WEEK = 7
private const val NO_EVENTS_IN_WEEK = 0
