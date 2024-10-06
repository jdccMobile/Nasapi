package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.Flow

class GetIfThereIsFavEventsUseCase(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    operator fun invoke(): Flow<Boolean> = astronomicEventRepository.thereIsFavEvents
}
