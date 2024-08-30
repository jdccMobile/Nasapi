package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError

interface AstronomicEventRepository {
    suspend fun getAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>>
}
