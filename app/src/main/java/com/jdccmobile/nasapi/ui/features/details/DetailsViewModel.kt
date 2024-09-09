package com.jdccmobile.nasapi.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventRepository
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
class DetailsViewModel(
//    private val astronomicEventId: AstronomicEventId,
    private val repository: AstronomicEventRepository,
) : ViewModel() {
    private val _isDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoading: StateFlow<Boolean> = _isDataLoading.asStateFlow()

    val astronomicEvent: StateFlow<AstronomicEventUi?> =
        repository.getAstronomicEventDetails(AstronomicEventId("ae20240902"))
            .mapLatest {
                _isDataLoading.value = false
                it.toUi()
            }
            .onStart { _isDataLoading.value = true }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onFavoriteFabClicked() {
        // TODO add to favorites
        // TODO add toast
    }

    fun onTakePhotoFabClicked() {
        // TODO open camera
    }
}
