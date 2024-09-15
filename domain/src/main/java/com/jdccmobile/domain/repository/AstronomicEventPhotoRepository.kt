package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.MyError
import kotlinx.coroutines.flow.Flow

interface AstronomicEventPhotoRepository {
    fun photosByEvent(eventId: AstronomicEventId): Flow<List<AstronomicEventPhoto>>

    suspend fun insertPhoto(photo: AstronomicEventPhoto): Either<MyError, Unit>

    suspend fun deletePhoto(photo: AstronomicEventPhoto): Either<MyError, Unit>
}
