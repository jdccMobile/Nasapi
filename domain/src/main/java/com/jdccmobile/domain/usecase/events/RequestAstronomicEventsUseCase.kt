package com.jdccmobile.domain.usecase.events

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository

class RequestAstronomicEventsUseCase(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String,
    ): Either<MyError, Unit> = either { astronomicEventRepository.requestAstronomicEvents(startDate, endDate).bind() }
}
