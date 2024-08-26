package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent

interface AstronomicEventRepository {
    suspend fun getAstronomicEvent(): Either<Throwable, AstronomicEvent>

    suspend fun getAstronomicEventsPerWeek(
        startDate: String,
        endDate: String,
    ): Either<Throwable, List<AstronomicEvent>>
}
