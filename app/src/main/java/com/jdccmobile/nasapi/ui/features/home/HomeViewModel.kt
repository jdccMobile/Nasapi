package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel : ViewModel() {
    private val _astronomicEvents: MutableStateFlow<List<AstronomicEventUi>> =
        MutableStateFlow(eventsMock)
    val astronomicalEvents: StateFlow<List<AstronomicEventUi>> =
        _astronomicEvents.asStateFlow()

    fun onAstronomicEventClicked() {
        // TODO navigate to details
    }

    fun onFavoritesClicked() {
        // TODO navigate to favorites
    }
}

data class AstronomicEventUi(
    val title: String,
    val description: String,
    val date: LocalDate, // Mirar tranformaciones de todate y totime
    val imageUrl: String,
)

private val eventsMock = List(10) {
    AstronomicEventUi(
        title = "Prueba $it",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
    )
}
