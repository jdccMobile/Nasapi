package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent

interface AstronomicEventRepository {
    suspend fun getAstronomicEvent(): Either<Throwable, AstronomicEvent>

//    suspend fun getAstronomicEventsPerWeek(): Either<Throwable, List<AstronomicEvent>>
}