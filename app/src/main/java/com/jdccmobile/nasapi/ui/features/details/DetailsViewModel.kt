package com.jdccmobile.nasapi.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.eventPhoto.DeletePhotoUseCase
import com.jdccmobile.domain.usecase.eventPhoto.GetPhotosByEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.InsertPhotoUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventUseCase
import com.jdccmobile.domain.usecase.events.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventPhotoUi
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toDomain
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class DetailsViewModel(
    astronomicEventId: String,
    private val screenActions: DetailsScreenActions,
    getAstronomicEventUseCase: GetAstronomicEventUseCase,
    getPhotosByEventUseCase: GetPhotosByEventUseCase,
    private val switchEventFavoriteStatusUseCase: SwitchEventFavoriteStatusUseCase,
    private val insertPhotoUseCase: InsertPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            isLoading = true,
            showCameraView = false,
            astronomicEvent = null,
            userPhotos = emptyList(),
        ),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAstronomicEventUseCase(AstronomicEventId(astronomicEventId)).collect{ event ->
                _uiState.update { it.copy(isLoading = false, astronomicEvent = event.toUi()) }
            }
        }
        viewModelScope.launch {
            getPhotosByEventUseCase(AstronomicEventId(astronomicEventId)).collect{ photos ->
                _uiState.update { it.copy(isLoading = false, userPhotos = photos.toUi()) }
            }
        }
    }

    fun onSwitchFavStatusClicked() {
        viewModelScope.launch {
            _uiState.value.astronomicEvent?.let { switchEventFavoriteStatusUseCase(it.toDomain()) }
        }
    }

    fun onOpenCameraClicked() {
        _uiState.update { it.copy(showCameraView = true) }
    }

    fun onSavePhotoTaken(photo: AstronomicEventPhotoUi, file: File, imageToSave: ByteArray) {
        viewModelScope.launch {
            insertPhotoUseCase(photo.toDomain(), file, imageToSave)
            _uiState.update { it.copy(showCameraView = false) }
        }
    }

    fun onDeletePhoto(photo: AstronomicEventPhotoUi) {
        viewModelScope.launch {
            deletePhotoUseCase(photo.toDomain())
        }
    }

    fun onCloseCamera() {
        _uiState.update { it.copy(showCameraView = false) }
    }

    fun onNavBack() {
        screenActions.onNavBack()
    }
}

data class UiState(
    val isLoading: Boolean,
    val showCameraView: Boolean,
    val astronomicEvent: AstronomicEventUi?,
    val userPhotos: List<AstronomicEventPhotoUi>,
)

data class DetailsScreenActions(
    val onNavBack: () -> Unit,
)
