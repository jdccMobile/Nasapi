package com.jdccmobile.nasapi.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val astronomicEventRepository: AstronomicEventRepository,
) : ViewModel() {
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

    init {
        viewModelScope.launch {
            val a = astronomicEventRepository.getAstronomicEvent()
            Log.d("asd", "a: $a")
        }
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
