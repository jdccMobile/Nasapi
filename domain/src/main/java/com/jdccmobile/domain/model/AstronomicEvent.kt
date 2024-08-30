package com.jdccmobile.domain.model

import java.time.LocalDate

data class AstronomicEvent(
    val id: AstronomicEventId,
    val title: String,
    val description: String,
    val date: LocalDate,
    val imageUrl: String?,
    val isFavorite: Boolean,
    val hasImage: Boolean,
)

data class AstronomicEventId(val value: String)
