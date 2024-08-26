package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.repository.AstronomicEventRepository

class GetAstronomicEvents(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String,
    ): Either<Throwable, List<AstronomicEvent>> =
        either {
            astronomicEventRepository.getAstronomicEventsPerWeek(startDate, endDate).bind()
        }
}
