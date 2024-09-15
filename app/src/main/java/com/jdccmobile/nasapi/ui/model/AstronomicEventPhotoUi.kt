package com.jdccmobile.nasapi.ui.model

import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import java.time.LocalDate

data class AstronomicEventPhotoUi(
    val photoId: AstronomicEventPhotoId,
    val eventId: AstronomicEventId,
    val filePath: String,
)

fun List<AstronomicEventPhoto>.toUi() = map { photo -> photo.toUi() }

fun AstronomicEventPhoto.toUi() = AstronomicEventPhotoUi(
    photoId = photoId,
    eventId = eventId,
    filePath = filePath,
)

fun AstronomicEventPhotoUi.toDomain() = AstronomicEventPhoto(
    photoId = photoId,
    eventId = eventId,
    filePath = filePath,
)
