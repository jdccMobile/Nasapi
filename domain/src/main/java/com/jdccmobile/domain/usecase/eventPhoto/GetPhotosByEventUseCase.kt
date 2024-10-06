package com.jdccmobile.domain.usecase.eventPhoto

import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import kotlinx.coroutines.flow.Flow

class GetPhotosByEventUseCase(
    private val astronomicEventPhotoRepository: AstronomicEventPhotoRepository,
) {
    operator fun invoke(eventId: AstronomicEventId): Flow<List<AstronomicEventPhoto>> =
        astronomicEventPhotoRepository.photosByEvent(eventId)
}
