package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow

class GetAstronomicEventUseCase(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    operator fun invoke(astronomicEventId: AstronomicEventId): Flow<AstronomicEvent> =
        astronomicEventRepository.getAstronomicEventDetails(astronomicEventId)
}
