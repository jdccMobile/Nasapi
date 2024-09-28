package com.jdccmobile.nasapi.ui.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.events.GetFavoriteAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val screenActions: FavoritesScreenActions,
    getFavoriteAstronomicEventsUseCase: GetFavoriteAstronomicEventsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            loading = true,
            favoriteEvents = emptyList(),
        ),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onFavoriteEventClicked(astronomicEventId: String) {
        screenActions.onNavToDetails(astronomicEventId)
    }

    fun onNavBack() {
        screenActions.onNavBack()
    }

    init {
        viewModelScope.launch {
            getFavoriteAstronomicEventsUseCase().collect { events ->
                _uiState.update { UiState(loading = false, favoriteEvents = events.toUi()) }
            }
        }
    }
}

data class UiState(
    val loading: Boolean,
    val favoriteEvents: List<AstronomicEventUi>,
)

data class FavoritesScreenActions(
    val onNavBack: () -> Unit,
    val onNavToDetails: (String) -> Unit,
)
