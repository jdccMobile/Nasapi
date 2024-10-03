package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.events.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetIfThereIsFavEventsUseCase
import com.jdccmobile.domain.usecase.events.RequestAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toUi
import com.jdccmobile.nasapi.ui.utils.getFirstDayOfWeek
import com.jdccmobile.nasapi.ui.utils.getLastDayOfWeek
import com.jdccmobile.nasapi.ui.utils.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val screenActions: HomeScreenActions,
    private val requestAstronomicEventsUseCase: RequestAstronomicEventsUseCase,
    getAstronomicEventsUseCase: GetAstronomicEventsUseCase,
    getIfThereIsFavEventsUseCase: GetIfThereIsFavEventsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            isInitialDataLoading = true,
            isMoreDataLoading = false,
            astronomicEvents = emptySet(),
            thereIsFavEvents = false,
            error = null,
        ),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        requestInitialEvents()
        viewModelScope.launch {
            getAstronomicEventsUseCase().collect { events ->
                _uiState.update { it.copy(astronomicEvents = events.toUi().toSet()) }
            }
        }
        viewModelScope.launch {
            getIfThereIsFavEventsUseCase().collect { thereIsFavEvents ->
                _uiState.update { it.copy(thereIsFavEvents = thereIsFavEvents) }
            }
        }
    }

    fun onLoadMoreItems() {
        if (!_uiState.value.isMoreDataLoading) {
            _uiState.update { it.copy(isMoreDataLoading = true) }
            viewModelScope.launch {
                nextWeekToLoad?.let { nextWeekToLoad ->
                    requestAstronomicEvents(
                        startDate = nextWeekToLoad.getFirstDayOfWeek(),
                        endDate = nextWeekToLoad.getLastDayOfWeek(),
                        loadingType = LoadingType.LoadingMoreData,
                    )
                }
                _uiState.update { it.copy(isMoreDataLoading = false) }
            }
        }
    }

    fun onAstronomicEventClicked(astronomicEventId: String) {
        screenActions.navigateToDetails(astronomicEventId)
    }

    fun onFavoritesClicked() {
        screenActions.navigateToFavorites()
    }

    private var nextWeekToLoad: LocalDate? = null

    fun requestInitialEvents() {
        viewModelScope.launch {
            requestAstronomicEvents(
                startDate = LocalDate.now().getFirstDayOfWeek(),
                endDate = LocalDate.now(),
                loadingType = LoadingType.InitialLoading,
            )
            _uiState.update { it.copy(isInitialDataLoading = false) }
        }
    }

    @Suppress("MagicNumber")
    private suspend fun requestAstronomicEvents(
        startDate: LocalDate,
        endDate: LocalDate,
        loadingType: LoadingType,
    ) {
        requestAstronomicEventsUseCase(
            startDate = startDate.toString(),
            endDate = endDate.toString(),
        ).fold(
            ifLeft = { error ->
                _uiState.update { it.copy(error = ErrorUi(error.toMessage(), loadingType)) }
            },
            ifRight = {
                nextWeekToLoad = startDate.minusWeeks(1)
                _uiState.update { it.copy(error = null) }
            },
        )
    }
}

data class UiState(
    val isInitialDataLoading: Boolean,
    val isMoreDataLoading: Boolean,
    val astronomicEvents: Set<AstronomicEventUi>,
    val thereIsFavEvents: Boolean,
    val error: ErrorUi?,
)

data class HomeScreenActions(
    val navigateToFavorites: () -> Unit,
    val navigateToDetails: (String) -> Unit,
)

data class ErrorUi(
    val message: String,
    val type: LoadingType,
)

enum class LoadingType {
    InitialLoading,
    LoadingMoreData,
}
