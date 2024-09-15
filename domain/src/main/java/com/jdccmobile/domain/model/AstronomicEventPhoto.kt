package com.jdccmobile.domain.model

import java.time.LocalDate

data class AstronomicEventPhoto(
    val photoId: AstronomicEventPhotoId,
    val eventId: AstronomicEventId,
    val filePath: String,
)

data class AstronomicEventPhotoId(val value: String)

