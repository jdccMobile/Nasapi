package com.jdccmobile.domain.model

import java.time.LocalDate

data class AstronomicEvent(
    val title: String,
    val description: String,
    val date: LocalDate,
    val imageUrl: String?,
)
