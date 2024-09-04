package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import kotlinx.coroutines.flow.Flow

interface AstronomicEventRepository {
    suspend fun getAstronomicEvents(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>>

    fun getFavoriteAstronomicEvents(): Flow<List<AstronomicEvent>>
}
