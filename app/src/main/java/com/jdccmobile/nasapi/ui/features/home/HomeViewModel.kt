package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.RequestAstronomicEventsUseCase
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
@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val requestAstronomicEventsUseCase: RequestAstronomicEventsUseCase,
    getAstronomicEventsUseCase: GetAstronomicEventsUseCase,
) : ViewModel() {
    val astronomicEvents: StateFlow<Set<AstronomicEventUi>> =
        getAstronomicEventsUseCase().mapLatest { events ->
            events.toUi().toSet()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    private val _isInitialDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInitialDataLoading: StateFlow<Boolean> = _isInitialDataLoading.asStateFlow()

    private val _isMoreDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMoreDataLoading: StateFlow<Boolean> = _isMoreDataLoading.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onLoadMoreItems() {
        if (!_isMoreDataLoading.value) {
            viewModelScope.launch {
                _isMoreDataLoading.value = true
                nextWeekToLoad?.let { nextWeekToLoad ->
                    requestAstronomicEvents(
                        startDate = nextWeekToLoad.getFirstDayOfWeek(),
                        endDate = nextWeekToLoad.getLastDayOfWeek(),
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
            )
            _isInitialDataLoading.value = false
        }
    }

    @Suppress("MagicNumber")
    private suspend fun requestAstronomicEvents(
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        requestAstronomicEventsUseCase(
            startDate = startDate.toString(),
            endDate = endDate.toString(),
        ).fold(
            ifLeft = { error ->
                _errorMessage.value = error.toMessage()
            },
            ifRight = { nextWeekToLoad = startDate.minusWeeks(1) },
        )
    }
}
