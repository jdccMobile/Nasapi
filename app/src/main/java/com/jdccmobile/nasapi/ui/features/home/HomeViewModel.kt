package com.jdccmobile.nasapi.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.data.local.AstronomicEventLocalDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.GetAstronomicEvents
import com.jdccmobile.nasapi.ui.utils.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress("ktlint:standard:property-naming") // TODO mirar como esta en la feina
class HomeViewModel(
    private val getAstronomicEvents: GetAstronomicEvents,
    private val localDataSource: AstronomicEventLocalDataSource,
) : ViewModel() {
    private val _astronomicEvents: MutableStateFlow<List<AstronomicEventUi>> =
        MutableStateFlow(emptyList())
    val astronomicEvents: StateFlow<List<AstronomicEventUi>> =
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
                nextEndDateToLoad?.let { nextEndDateToLoad ->
                    getAstronomicEventsUi(
                        startDate = nextEndDateToLoad.minusDays(ASTRONOMIC_EVENT_NUMBER_TO_LOAD),
                        endDate = nextEndDateToLoad,
                    )
                }
                _isMoreDataLoading.value = false
            }
        }
    }

    fun onFavoritesClicked() {
        viewModelScope.launch {
            // TODO add correct logic
            localDataSource.insertAstronomicEvent(astronomicEvents.value.first().toDomain())
        }
    }

    fun AstronomicEventUi.toDomain(): AstronomicEvent = AstronomicEvent(
        id = id,
        title = title,
        description = description,
        date = date,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        hasImage = hasImage,
    )


    init {
        getInitialEvents()
    }

    private var nextEndDateToLoad: LocalDate? = null

    private fun getInitialEvents() {
        viewModelScope.launch {
            _isInitialDataLoading.value = true
            getAstronomicEventsUi(
                startDate = LocalDate.now().minusDays(ASTRONOMIC_EVENT_NUMBER_TO_LOAD),
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
        getAstronomicEvents(
            startDate = startDate.toString(),
            endDate = endDate.toString(),
        ).fold(
            ifLeft = { error ->
                _errorMessage.value = error.toMessage()
            },
            ifRight = { data ->
                _astronomicEvents.value += data.reversed().toUi()
                nextEndDateToLoad = startDate.minusDays(1)
            },
        )
    }
}

// Mirar tranformaciones de todate y totime
data class AstronomicEventUi(
    val id: AstronomicEventId,
    val title: String,
    val description: String,
    val date: LocalDate,
    val imageUrl: String?,
    val isFavorite: Boolean,
    val hasImage: Boolean,
)

private fun List<AstronomicEvent>.toUi(): List<AstronomicEventUi> = map {
    AstronomicEventUi(
        id = it.id,
        title = it.title,
        description = it.description,
        date = it.date,
        imageUrl = it.imageUrl,
        isFavorite = it.isFavorite,
        hasImage = it.hasImage,
    )
}

private const val ASTRONOMIC_EVENT_NUMBER_TO_LOAD: Long = 7
