package com.jdccmobile.data.repository

import arrow.core.Either
import arrow.core.flatMap
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class AstronomicEventRepositoryImpl(
    private val localDataSource: AstronomicEventLocalDataSource,
    private val requestAndInsertEventsPerWeek: RequestAndInsertEventsPerWeek,
) : AstronomicEventRepository {

    override val astronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.astronomicEvents()

    override val favoriteAstronomicEvents: Flow<List<AstronomicEvent>> =
        localDataSource.favoriteAstronomicEvents()

    override fun getAstronomicEventDetails(astronomicEventId: AstronomicEventId): Flow<AstronomicEvent> =
        localDataSource.getAstronomicEvent(astronomicEventId)

    override suspend fun requestAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> {
        localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { return Either.Left(it) },
            ifRight = { eventsInDb ->
                when (eventsInDb.size) {
                    EVENTS_IN_WEEK -> {}
                    NO_EVENTS_IN_WEEK -> requestAndInsertEventsPerWeek.allEvents(startDate, endDate)
                    else -> requestAndInsertEventsPerWeek.specificEvents(startDate, endDate)
                }
            },
        )
        return Either.Right(Unit)
    }

    override suspend fun switchFavoriteStatus(
        astronomicEvent: AstronomicEvent
    ): Either<MyError, Unit> = localDataSource.switchFavoriteStatus(astronomicEvent).map {
        println("asd asd")
    }

}

private const val EVENTS_IN_WEEK = 7
private const val NO_EVENTS_IN_WEEK = 0
