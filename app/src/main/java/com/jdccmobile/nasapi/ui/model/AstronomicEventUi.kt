package com.jdccmobile.nasapi.ui.model

import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import java.time.LocalDate

data class AstronomicEventUi(
    val id: AstronomicEventId,
    val title: String,
    val description: String,
    val date: LocalDate,
    val imageUrl: String?,
    val isFavorite: Boolean,
    val hasImage: Boolean,
)

fun List<AstronomicEvent>.toUi(): List<AstronomicEventUi> = map {
    AstronomicEventUi(
        id = it.id,
        title = it.title,
        description = it.description,
        date = it.date,
        imageUrl = it.imageUrl,
        isFavorite = it.isFavorite,
        hasImage = it.hasImage,
    )
}

fun AstronomicEvent.toUi(): AstronomicEventUi = AstronomicEventUi(
    id = id,
    title = title,
    description = description,
    date = date,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
    hasImage = hasImage,
)

fun AstronomicEventUi.toDomain(): AstronomicEvent = AstronomicEvent(
    id = id,
    title = title,
    description = description,
    date = date,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
    hasImage = hasImage,
)
