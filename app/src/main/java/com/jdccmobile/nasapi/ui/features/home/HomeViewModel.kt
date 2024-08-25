package com.jdccmobile.nasapi.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.domain.usecase.GetAstronomicEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val getAstronomicEvents: GetAstronomicEvents,
) : ViewModel() {
    private val _isDataLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded.asStateFlow()

    private val _astronomicEvents: MutableStateFlow<List<AstronomicEventUi>> =
        MutableStateFlow(emptyList())
    val astronomicalEvents: StateFlow<List<AstronomicEventUi>> =
        _astronomicEvents.asStateFlow()

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
                // TODO añadir _isdataloaded a true y mostrar error
                ifLeft = { Log.e("asd", "Error", it) },
                ifRight = {
                    _astronomicEvents.value = it.toUi()
                    _isDataLoaded.value = true
                },
            )
        }
    }
}

data class AstronomicEventUi(
    val title: String,
    val description: String,
    val date: LocalDate, // Mirar tranformaciones de todate y totime
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
