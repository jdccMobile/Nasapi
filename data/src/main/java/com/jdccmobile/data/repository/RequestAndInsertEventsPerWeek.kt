package com.jdccmobile.data.repository

import arrow.core.Either
import arrow.core.flatMap
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.MyError
import java.time.LocalDate

class RequestAndInsertEventsPerWeek(
    private val remoteDataSource: AstronomicEventRemoteDataSource,
    private val localDataSource: AstronomicEventLocalDataSource,
) {
    suspend fun allEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> =
        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).flatMap { events ->
            localDataSource.insertAstronomicEventList(events)
        }

    @Suppress("ReturnCount")
    suspend fun specificEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> {
        val datesToCheck = generateDatesBetween(LocalDate.parse(startDate), LocalDate.parse(endDate))
        val datesWithoutEvents = mutableListOf<String>()

        for (date in datesToCheck) {
            localDataSource.hasEventForDate(date.toString()).fold(
                ifLeft = { return Either.Left(it) },
                ifRight = { hasEvent ->
                    if (!hasEvent) datesWithoutEvents.add(date.toString())
                },
            )
        }

        for (date in datesWithoutEvents) {
            remoteDataSource.getAstronomicEvent(date).fold(
                ifLeft = { return Either.Left(it) },
                ifRight = { event ->
                    localDataSource.insertAstronomicEvent(event).fold(
                        ifLeft = { return Either.Left(it) },
                        ifRight = {},
                    )
                },
            )
        }

        return Either.Right(Unit)
    }
}

private fun generateDatesBetween(
    startDate: LocalDate,
    endDate: LocalDate,
): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDate = startDate

    while (!currentDate.isAfter(endDate)) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    return dates
}
