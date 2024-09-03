package com.jdccmobile.nasapi.ui.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.ui.features.home.AstronomicEventUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class FavoritesViewModel() : ViewModel() {
    private val _favoriteEvents: MutableStateFlow<Set<AstronomicEventUi>> =
        MutableStateFlow(emptySet())
    val favoriteEvents: StateFlow<Set<AstronomicEventUi>> =
        _favoriteEvents.asStateFlow()

    private val _isInitialDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInitialDataLoading: StateFlow<Boolean> = _isInitialDataLoading.asStateFlow()

    fun onFaoriteEventClicked() {
        // TODO navigate to details
    }

    init {
        getFavoriteEvents()
    }

    private fun getFavoriteEvents() {
        viewModelScope.launch {
            _isInitialDataLoading.value = true
            _favoriteEvents.value = getFavoriteEventsMock() // TODO invoke usecase
            _isInitialDataLoading.value = false
        }
    }
}

private fun getFavoriteEventsMock() = List(5) {
    AstronomicEventUi(
        id = AstronomicEventId("id$it"),
        title = "title$it",
        description = "description$it",
        date = LocalDate.now(),
        hasImage = true,
        imageUrl = null,
        isFavorite = true,
    )
}.toSet()
