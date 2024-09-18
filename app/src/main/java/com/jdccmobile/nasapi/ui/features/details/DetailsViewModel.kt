package com.jdccmobile.nasapi.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.eventPhoto.DeletePhotoUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.GetPhotosByEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.InsertPhotoUseCase
import com.jdccmobile.domain.usecase.events.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventPhotoUi
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
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModel(
    astronomicEventId: String,
    getAstronomicEventUseCase: GetAstronomicEventUseCase,
    getPhotosByEventUseCase: GetPhotosByEventUseCase,
    private val switchEventFavoriteStatusUseCase: SwitchEventFavoriteStatusUseCase,
    private val insertPhotoUseCase: InsertPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
) : ViewModel() {
    private val _isDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDataLoading: StateFlow<Boolean> = _isDataLoading.asStateFlow()

    private val _showCameraView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showCameraView: StateFlow<Boolean> = _showCameraView.asStateFlow()

    val astronomicEvent: StateFlow<AstronomicEventUi?> =
        getAstronomicEventUseCase(
            AstronomicEventId(astronomicEventId),
        ).mapLatest {
            _isDataLoading.value = false
            it.toUi()
        }
            .onStart { _isDataLoading.value = true }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val userPhotos: StateFlow<List<AstronomicEventPhotoUi>> =
        getPhotosByEventUseCase(AstronomicEventId(astronomicEventId)) // todo
            .mapLatest { it.toUi() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onSwitchFavStatusClicked() {
        viewModelScope.launch {
            astronomicEvent.value?.let { switchEventFavoriteStatusUseCase(it.toDomain()) }
        }
    }

    fun onOpenCameraClicked() {
        _showCameraView.value = true
    }

    fun onSavePhotoTaken(photo: AstronomicEventPhotoUi, file: File, imageToSave: ByteArray) {
        viewModelScope.launch {
            insertPhotoUseCase(photo.toDomain(), file, imageToSave)
            _showCameraView.value = false
        }
    }

    fun onDeletePhoto(photo: AstronomicEventPhotoUi) {
        viewModelScope.launch {
            deletePhotoUseCase(photo.toDomain())
        }
    }
}
