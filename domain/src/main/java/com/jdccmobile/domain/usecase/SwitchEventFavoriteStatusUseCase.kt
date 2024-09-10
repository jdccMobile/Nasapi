package com.jdccmobile.domain.usecase

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventRepository

class SwitchEventFavoriteStatusUseCase(
    private val astronomicEventRepository: AstronomicEventRepository,
) {
    suspend operator fun invoke(astronomicEvent: AstronomicEvent): Either<MyError, Unit> =
        astronomicEventRepository.switchFavoriteStatus(astronomicEvent)
}
