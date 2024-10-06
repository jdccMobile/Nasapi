package com.jdccmobile.data.remote.datasource

import arrow.core.Either
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.model.AstronomicEventResponse
import com.jdccmobile.data.utils.toMyError
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError
import java.time.LocalDate

class AstronomicEventRemoteDataSource(
    private val apiKey: String,
    private val service: RetrofitService,
) {
    suspend fun getAstronomicEvent(date: String): Either<MyError, AstronomicEvent> =
        Either.catch { service.getAstronomicEvent(apiKey, date).toDomain() }
            .mapLeft { it.toMyError() }

    suspend fun getAstronomicEventsPerWeek(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        Either.catch {
            service.getAstronomicEventsPerWeek(apiKey, startDate, endDate).map { it.toDomain() }
        }.mapLeft { it.toMyError() }
}

private fun AstronomicEventResponse.toDomain(): AstronomicEvent =
    AstronomicEvent(
        id = AstronomicEventId("ae" + date.replace("-", "")),
        title = title,
        description = explanation,
        date = LocalDate.parse(date),
        imageUrl =
            when (mediaType) {
                "image" -> hdUrl ?: url
                "video" -> null // TODO put gif?
                else -> null
            },
        isFavorite = false,
        hasImage = false,
    )
