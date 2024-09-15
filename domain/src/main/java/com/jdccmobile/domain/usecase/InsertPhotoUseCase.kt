package com.jdccmobile.domain.usecase

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository

class InsertPhotoUseCase(
    private val astronomicEventPhotoRepository: AstronomicEventPhotoRepository,
) {
    suspend operator fun invoke(photo: AstronomicEventPhoto): Either<MyError, Unit>
    = astronomicEventPhotoRepository.insertPhoto(photo)
}
