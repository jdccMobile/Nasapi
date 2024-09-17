package com.jdccmobile.nasapi.ui.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.events.GetFavoriteAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    getFavoriteAstronomicEventsUseCase: GetFavoriteAstronomicEventsUseCase,
) : ViewModel() {
    private val _isDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoading: StateFlow<Boolean> = _isDataLoading.asStateFlow()

    val favoriteEvents: StateFlow<List<AstronomicEventUi>> =
        getFavoriteAstronomicEventsUseCase()
            .mapLatest {
                _isDataLoading.value = false
                it.toUi()
            }
            .onStart { _isDataLoading.value = true }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onFavoriteEventClicked() {
        // TODO navigate to details
    }
}
