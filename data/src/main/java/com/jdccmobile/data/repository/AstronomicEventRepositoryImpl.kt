package com.jdccmobile.data.repository

import androidx.annotation.RequiresApi
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
    // TODO corregir
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

    // TODO Crear otro metodo que obtenga fechas en especifico
    private suspend fun requestAndInsertEventsPerWeek(
        startDate: String,
        endDate: String
    ): Either<MyError, Unit> =
        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).fold(
            ifLeft = { Either.Left(it) },
            ifRight = { events ->
                println("asd: generic")
                localDataSource.insertAstronomicEventList(events) }
        )

    private suspend fun requestAndInsertSpecificEventsPerWeek(
        startDate: String,
        endDate: String
    ): Either<MyError, Unit> {
        val datesToSearch = generateDatesBetween(LocalDate.parse(startDate), LocalDate.parse(endDate))
        println("asd: especificos")

        val notEventsInDb = mutableListOf<String>()

        // Iterar sobre las fechas y verificar si hay eventos en la base de datos
        for (date in datesToSearch) {
            when (val result = localDataSource.hasEventForDate(date.toString())) {
                is Either.Left -> {
                    // Manejar error de base de datos
                    return Either.Left(result.value)
                }
                is Either.Right -> {
                    if (!result.value) {
                        // Si no hay evento en la base de datos, agregar la fecha a la lista
                        notEventsInDb.add(date.toString())
                    }
                }
            }
        }
        println("asd: notEventsInDb: $notEventsInDb")

        // Ahora puedes usar `notEventsInDb` para hacer las peticiones a la API
        for (date in notEventsInDb) {
            when (val result = remoteDataSource.getAstronomicEvent(date)) {
                is Either.Left -> {
                    // Manejar error de la API si es necesario
                    return Either.Left(result.value)
                }
                is Either.Right -> {
                    // Insertar el evento recuperado en la base de datos
                    when (val insertResult = localDataSource.insertAstronomicEvent(result.value)) {
                        is Either.Left -> return Either.Left(insertResult.value)  // Manejar error al insertar
                        is Either.Right -> {}  // Continuar si la inserción fue exitosa
                    }
                }
            }
        }
        // Devolver `Either.Right(Unit)` si todo va bien
        return Either.Right(Unit)
    }
//        remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate).fold(
//            ifLeft = { Either.Left(it) },
//            ifRight = { events -> localDataSource.insertAstronomicEventList(events) }
//        )
}

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