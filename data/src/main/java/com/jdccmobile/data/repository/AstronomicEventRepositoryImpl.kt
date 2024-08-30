package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.remote.datasource.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AstronomicEventRepositoryImpl(
    private val remoteDataSource: AstronomicEventRemoteDataSource,
    private val localDataSource: AstronomicEventLocalDataSource,
) : AstronomicEventRepository {
    // TODO hasta cuadno arrastar el localdate y utilizar formatter por si les da por cambiar en la api
    override suspend fun getInitialAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> {
        val expectedEvents =
            ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate)).toInt()
        return localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { error -> Either.Left(error) },
            ifRight = { eventsInDb ->
                println("asd: ${eventsInDb.size}")
                if (eventsInDb.size == expectedEvents) {
                    Either.Right(eventsInDb)
                } else {
                    requestAndInsertEventsPerWeek(startDate, endDate).fold(
                        ifLeft = { error -> Either.Left(error) },
                        ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
                    )
                }
            }
        )
    }


    override suspend fun getAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        localDataSource.getAstronomicEventList(startDate, endDate).fold(
            ifLeft = { error -> Either.Left(error) },
            ifRight = { eventsInDb ->
                when (eventsInDb.size) {
                    EVENTS_IN_WEEK -> {
                        Either.Right(eventsInDb)
                    }

                    NO_EVENTS_IN_WEEK -> {
                        requestAndInsertEventsPerWeek(startDate, endDate).fold(
                            ifLeft = { error -> Either.Left(error) },
                            ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
                        )
                    }

                    else -> {
                        requestAndInsertSpecificEventsPerWeek(startDate, endDate).fold(
                            ifLeft = { error -> Either.Left(error) },
                            ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
                        )
                    }
                }
            }
        )

    private suspend fun requestAndInsertEventsPerWeek(
        startDate: String,
        endDate: String
    ): Either<MyError, Unit> =
        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).fold(
            ifLeft = { Either.Left(it) },
            ifRight = { events ->
                localDataSource.insertAstronomicEventList(events)
            }
        )

    private suspend fun requestAndInsertSpecificEventsPerWeek(
        startDate: String,
        endDate: String
    ): Either<MyError, Unit> {
        val datesToCheck =
            generateDatesBetween(LocalDate.parse(startDate), LocalDate.parse(endDate))

        val notEventInDb = mutableListOf<String>()

        for (date in datesToCheck) {
            localDataSource.hasEventForDate(date.toString()).fold(
                ifLeft = { return Either.Left(it) },
                ifRight = { hasEvent ->
                    if (!hasEvent) {
                        notEventInDb.add(date.toString())
                    }
                }
            )
        }

        for (date in notEventInDb) {
            remoteDataSource.getAstronomicEvent(date).fold(
                ifLeft = { return Either.Left(it) },
                ifRight = { event ->
                    localDataSource.insertAstronomicEvent(event).fold(
                        ifLeft = { return Either.Left(it) },
                        ifRight = {}
                    )
                }
            )
        }
        return Either.Right(Unit)
    }
}

// TODO esto psarlo al usecase, y utilizar el first y end y el resto cuando se necesite
// TODO crear modulo de commons para utilidades para que tengan acceso todos los modulos
private fun generateDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDate = startDate

    while (!currentDate.isAfter(endDate)) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    return dates
}

// Función para obtener las fechas dentro de un rango
//private fun getDatesInRange(startDate: String, endDate: String): List<String> {
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val start = LocalDate.parse(startDate, formatter)
//    val end = LocalDate.parse(endDate, formatter)
//
//    return start.datesUntil(end.plusDays(1)).map { it.toString() }.toList()
//}


private const val EVENTS_IN_WEEK = 7
private const val NO_EVENTS_IN_WEEK = 0


//                if (eventsInDb.size == EVENTS_IN_WEEK) {
//                    Either.Right(eventsInDb)
//
//                } else if (eventsInDb.isEmpty()) {
//                    requestAndInsertEvents(startDate, endDate).fold(
//                        ifLeft = { error -> Either.Left(error) },
//                        ifRight = { localDataSource.getAstronomicEventList(startDate, endDate) }
//                    )
//                } else {
//                     TODO llamar al nuevo metodo que busca cuales no estan en la bbdd y los decagrfa
//                    Either.Right(eventsInDb)
//                }