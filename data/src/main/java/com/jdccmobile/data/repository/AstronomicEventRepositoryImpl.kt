package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow

class AstronomicEventRepositoryImpl(
    private val localDataSource: AstronomicEventLocalDataSource,
    private val requestAndInsertEventsPerWeek: RequestAndInsertEventsPerWeek,
) : AstronomicEventRepository {

    override val astronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.astronomicEvents()

    override val favoriteAstronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.favoriteAstronomicEvents()

    override val thereIsFavEvents: Flow<Boolean> = localDataSource.getIfThereIsFavEvents()

    override fun getAstronomicEventDetails(astronomicEventId: AstronomicEventId): Flow<AstronomicEvent> =
        localDataSource.getAstronomicEvent(astronomicEventId)

    override suspend fun requestAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> {
        return localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { return Either.Left(it) },
            ifRight = { eventsInDb ->
                when (eventsInDb.size) {
                    EVENTS_IN_WEEK -> Either.Right(Unit)
                    NO_EVENTS_IN_WEEK -> requestAndInsertEventsPerWeek.allEvents(startDate, endDate)
                    else -> requestAndInsertEventsPerWeek.specificEvents(startDate, endDate)
                }
            },
        )
    }

    override suspend fun switchFavoriteStatus(
        astronomicEvent: AstronomicEvent
    ): Either<MyError, Unit> = localDataSource.switchFavoriteStatus(astronomicEvent)
}

const val EVENTS_IN_WEEK = 7
const val NO_EVENTS_IN_WEEK = 0
