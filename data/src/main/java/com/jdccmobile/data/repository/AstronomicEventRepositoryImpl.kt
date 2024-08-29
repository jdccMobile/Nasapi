package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository

class AstronomicEventRepositoryImpl(
    private val remoteDataSource: AstronomicEventRemoteDataSource,
    private val localDataSource: AstronomicEventLocalDataSource,
) : AstronomicEventRepository {
    // TODO corregir
    override suspend fun getInitialAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { error -> Either.Left(error) },
            ifRight = { eventsInDb ->
                println("asd: ${eventsInDb.size}")
                if (eventsInDb.size == EVENTS_IN_WEEK) {
                    Either.Right(eventsInDb)
                } else {
                    requestAndInsertEvents(startDate, endDate).fold(
                        ifLeft = { error -> Either.Left(error) },
                        ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
                    )
                }
            }
        )

    override suspend fun getAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { error -> Either.Left(error) },
            ifRight = { eventsInDb ->
                println("asd: ${eventsInDb.size}")
                if (eventsInDb.size == EVENTS_IN_WEEK) {
                    Either.Right(eventsInDb)
                } else {
                    requestAndInsertEvents(startDate, endDate).fold(
                        ifLeft = { error -> Either.Left(error) },
                        ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
                    )
                }
            }
        )

    private suspend fun requestAndInsertEvents(
        startDate: String,
        endDate: String
    ): Either<MyError, Unit> =
        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).fold(
            ifLeft = { Either.Left(it) },
            ifRight = { events -> localDataSource.insertAstronomicEventList(events) }
        )
}


private const val EVENTS_IN_WEEK = 7