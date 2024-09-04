package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.GetAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toUi
import com.jdccmobile.nasapi.ui.utils.getFirstDayOfWeek
import com.jdccmobile.nasapi.ui.utils.getLastDayOfWeek
import com.jdccmobile.nasapi.ui.utils.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val getAstronomicEventsUseCase: GetAstronomicEventsUseCase,
) : ViewModel() {
    // TODO mirar state in (linkedin antonio leiva)
    private val _astronomicEvents: MutableStateFlow<Set<AstronomicEventUi>> =
        MutableStateFlow(emptySet())
    val astronomicEvents: StateFlow<Set<AstronomicEventUi>> =
        _astronomicEvents.asStateFlow()

    private val _isInitialDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInitialDataLoading: StateFlow<Boolean> = _isInitialDataLoading.asStateFlow()

    private val _isMoreDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMoreDataLoading: StateFlow<Boolean> = _isMoreDataLoading.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onAstronomicEventClicked() {
        // TODO navigate to details
    }

    fun onLoadMoreItems() {
        if (!_isMoreDataLoading.value) {
            viewModelScope.launch {
                _isMoreDataLoading.value = true
                nextWeekToLoad?.let { nextWeekToLoad ->
                    getAstronomicEventsUi(
                        startDate = nextWeekToLoad.getFirstDayOfWeek(),
                        endDate = nextWeekToLoad.getLastDayOfWeek(),
                    )
                }
                _isMoreDataLoading.value = false
            }
        }
    }

    fun onFavoritesClicked() {
        // TODO
    }

    init {
        getInitialEvents()
    }

    private var nextWeekToLoad: LocalDate? = null

    private fun getInitialEvents() {
        viewModelScope.launch {
            _isInitialDataLoading.value = true
            getAstronomicEventsUi(
                startDate = LocalDate.now().getFirstDayOfWeek(),
                endDate = LocalDate.now(),
            )
            _isInitialDataLoading.value = false
        }
    }

    @Suppress("MagicNumber")
    private suspend fun getAstronomicEventsUi(
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        getAstronomicEventsUseCase(
            startDate = startDate.toString(),
            endDate = endDate.toString(),
        ).fold(
            ifLeft = { error ->
                _errorMessage.value = error.toMessage()
            },
            ifRight = { data ->
                _astronomicEvents.value += data.reversed().toUi()
                nextWeekToLoad = startDate.minusWeeks(1)
            },
        )
    }
}
