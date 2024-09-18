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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val requestAstronomicEventsUseCase: RequestAstronomicEventsUseCase,
    getAstronomicEventsUseCase: GetAstronomicEventsUseCase,
    getIfThereIsFavEventsUseCase: GetIfThereIsFavEventsUseCase,
) : ViewModel() {
    val astronomicEvents: StateFlow<Set<AstronomicEventUi>> =
        getAstronomicEventsUseCase().mapLatest { events ->
            events.toUi().toSet()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val thereIsFavEvents: StateFlow<Boolean> = getIfThereIsFavEventsUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _isInitialDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInitialDataLoading: StateFlow<Boolean> = _isInitialDataLoading.asStateFlow()

    private val _isMoreDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMoreDataLoading: StateFlow<Boolean> = _isMoreDataLoading.asStateFlow()

    private val _error: MutableStateFlow<ErrorUi?> = MutableStateFlow(null)
    val error: StateFlow<ErrorUi?> = _error.asStateFlow()

    fun onLoadMoreItems() {
        if (!_isMoreDataLoading.value) {
            viewModelScope.launch {
                _isMoreDataLoading.value = true
                nextWeekToLoad?.let { nextWeekToLoad ->
                    requestAstronomicEvents(
                        startDate = nextWeekToLoad.getFirstDayOfWeek(),
                        endDate = nextWeekToLoad.getLastDayOfWeek(),
                        LoadingType.LoadingMoreData,
                    )
                }
                _isMoreDataLoading.value = false
            }
        }
    }

    fun onAstronomicEventClicked() {
        // TODO navigate to details
    }

    fun onFavoritesClicked() {
        // TODO navigate to favorites
    }

    init {
        requestInitialEvents()
    }

    private var nextWeekToLoad: LocalDate? = null

    private fun requestInitialEvents() {
        viewModelScope.launch {
            _isInitialDataLoading.value = true
            requestAstronomicEvents(
                startDate = LocalDate.now().getFirstDayOfWeek(),
                endDate = LocalDate.now(),
                loadingType = LoadingType.InitialLoading,
            )
            _isInitialDataLoading.value = false
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
                _error.value = ErrorUi(error.toMessage(), loadingType)
            },
            ifRight = {
                nextWeekToLoad = startDate.minusWeeks(1)
                _error.value = null
            },
        )
    }
}

data class ErrorUi(
    val message: String,
    val type: LoadingType,
)

enum class LoadingType {
    InitialLoading,
    LoadingMoreData
}
