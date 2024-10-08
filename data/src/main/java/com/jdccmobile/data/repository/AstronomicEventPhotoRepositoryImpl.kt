package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventPhotoLocalDataSource
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class AstronomicEventPhotoRepositoryImpl(
    private val localDataSource: AstronomicEventPhotoLocalDataSource,
) : AstronomicEventPhotoRepository {
    override fun photosByEvent(eventId: AstronomicEventId): Flow<List<AstronomicEventPhoto>> =
        localDataSource.getPhotosByEvent(eventId)

    override suspend fun insertPhoto(
        photo: AstronomicEventPhoto, file: File, imageToSave: ByteArray
    ): Either<MyError, Unit> =
        localDataSource.insertPhoto(photo, file, imageToSave)

    override suspend fun deletePhoto(photo: AstronomicEventPhoto): Either<MyError, Unit> =
        localDataSource.deletePhoto(photo)
}
