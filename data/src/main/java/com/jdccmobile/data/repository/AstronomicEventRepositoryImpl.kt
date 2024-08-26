package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.remote.AstronomicEventRemoteDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository

class AstronomicEventRepositoryImpl(
    private val remoteDataSource: AstronomicEventRemoteDataSource,
) : AstronomicEventRepository {
    override suspend fun getAstronomicEvent(): Either<MyError, AstronomicEvent> = remoteDataSource.getAstronomicEvent()

    override suspend fun getAstronomicEventsPerWeek(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> = remoteDataSource.getAstronomicEventsPerWeek(startDate, endDate)
}
