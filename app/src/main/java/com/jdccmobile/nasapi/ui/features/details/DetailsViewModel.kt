package com.jdccmobile.nasapi.ui.features.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.data.local.model.AstronomicEventPhotoDb
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.domain.usecase.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toDomain
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModel(
//    private val astronomicEventId: AstronomicEventId, // todo
    private val repository: AstronomicEventRepository,
    private val switchEventFavoriteStatusUseCase: SwitchEventFavoriteStatusUseCase,
    private val localDataSource: AstronomicEventLocalDataSource
) : ViewModel() {
    private val _isDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoading: StateFlow<Boolean> = _isDataLoading.asStateFlow()

    private val _showCameraView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showCameraView: StateFlow<Boolean> = _showCameraView.asStateFlow()

    val astronomicEvent: StateFlow<AstronomicEventUi?> =
        repository.getAstronomicEventDetails(
            AstronomicEventId("ae20240914"), // todo
        ) // todo astronomicEventId
            .mapLatest {
                _isDataLoading.value = false
                it.toUi()
            }
            .onStart { _isDataLoading.value = true }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val userPhotos: StateFlow<List<AstronomicEventPhotoDb>> =
        localDataSource.getPhotosByEvent(
            AstronomicEventId("ae20240914").value, // todo
        ).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onFavoriteFabClicked() {
        viewModelScope.launch {
            Log.i("asd", astronomicEvent.toString())
            astronomicEvent.value?.let { switchEventFavoriteStatusUseCase(it.toDomain()) }
        }
        // TODO add toast
    }

    fun onTakePhotoFabClicked() {
        _showCameraView.value = true
    }

    fun onPhotoTakenToDb(photoDb: AstronomicEventPhotoDb) {
        viewModelScope.launch {
            localDataSource.insertPhoto(photoDb) // TODO crear usecase
            _showCameraView.value = false
        }
    }

    fun onDeletePhoto(photoDb: AstronomicEventPhotoDb) {
        viewModelScope.launch {
            localDataSource.deletePhoto(photoDb) // TODO crear usecase actualizar tambien el has foto
        }
    }
}



