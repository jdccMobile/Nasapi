package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.usecase.GetAstronomicEvents
import com.jdccmobile.nasapi.ui.utils.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val getAstronomicEvents: GetAstronomicEvents,
) : ViewModel() {
    private val _astronomicEvents: MutableStateFlow<List<AstronomicEventUi>> =
        MutableStateFlow(emptyList())
    val astronomicalEvents: StateFlow<List<AstronomicEventUi>> =
        _astronomicEvents.asStateFlow()

    private val _isDataLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded.asStateFlow()

    // TODO ponerlo a null si hay exito si añado la opcion de recargar
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onAstronomicEventClicked() {
        // TODO navigate to details
    }

    fun onFavoritesClicked() {
        // TODO navigate to favorites
    }

    init {
        getInitialEvents()
    }

    private fun getInitialEvents() {
        viewModelScope.launch {
            // Todo obtener fechas mediante paging
            getAstronomicEvents("2024-08-10", "2024-08-17").fold(
                ifLeft = { error ->
                    _errorMessage.value = error.toMessage()
                    _isDataLoaded.value = true
                },
                ifRight = { data ->
                    _astronomicEvents.value = data.toUi()
                    _isDataLoaded.value = true
                },
            )
        }
    }
}

// Mirar tranformaciones de todate y totime
data class AstronomicEventUi(
    val title: String,
    val description: String,
    val date: LocalDate,
    val imageUrl: String?,
)

private fun List<AstronomicEvent>.toUi(): List<AstronomicEventUi> = map {
    AstronomicEventUi(
        title = it.title,
        description = it.description,
        date = it.date,
        imageUrl = it.imageUrl,
    )
}
