package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError
import kotlinx.coroutines.flow.Flow

interface AstronomicEventRepository {
    val astronomicEvents: Flow<List<AstronomicEvent>>
    val favoriteAstronomicEvents: Flow<List<AstronomicEvent>>

    fun getAstronomicEventDetails(astronomicEventId: AstronomicEventId): Flow<AstronomicEvent>

    suspend fun requestAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit>
}
