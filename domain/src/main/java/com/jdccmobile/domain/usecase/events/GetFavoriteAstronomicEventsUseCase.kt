package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteAstronomicEventsUseCase(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    operator fun invoke(): Flow<List<AstronomicEvent>> = astronomicEventRepository.favoriteAstronomicEvents
}
