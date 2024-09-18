package com.jdccmobile.domain.usecase.eventPhoto

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import java.io.File

class InsertPhotoUseCase(
    private val astronomicEventPhotoRepository: AstronomicEventPhotoRepository,
) {
    suspend operator fun invoke(photo: AstronomicEventPhoto, file: File, imageToSave: ByteArray): Either<MyError, Unit>
    = astronomicEventPhotoRepository.insertPhoto(photo, file, imageToSave)
}
