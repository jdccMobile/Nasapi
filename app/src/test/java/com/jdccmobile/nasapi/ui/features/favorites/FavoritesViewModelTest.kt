package com.jdccmobile.nasapi.ui.features.favorites

import app.cash.turbine.test
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.events.GetFavoriteAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {
    @Mock
    lateinit var getFavoriteAstronomicEventsUseCase: GetFavoriteAstronomicEventsUseCase

    @Mock
    lateinit var screenActions: FavoritesScreenActions

    private lateinit var viewModel: FavoritesViewModel

    private val favoriteEvents = listOf(
        AstronomicEvent(
            id = AstronomicEventId("1"),
            title = "Prueba",
            description = "Descripcion",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        ),
        AstronomicEvent(
            id = AstronomicEventId("2"),
            title = "Prueba",
            description = "Descripcion",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        ),
    )

    private val favoriteEventsUi = listOf(
        AstronomicEventUi(
            id = AstronomicEventId("1"),
            title = "Prueba",
            description = "Descripcion",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        ),
        AstronomicEventUi(
            id = AstronomicEventId("2"),
            title = "Prueba",
            description = "Descripcion",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        ),
    )

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getFavoriteAstronomicEventsUseCase()).thenReturn(flowOf(favoriteEvents))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `UI is updated with favorite events on start`() = runTest {
        viewModel = FavoritesViewModel(screenActions, getFavoriteAstronomicEventsUseCase)

        viewModel.uiState.test {
            assertEquals(UiState(loading = true, favoriteEvents = emptyList()), awaitItem())
            assertEquals(UiState(loading = false, favoriteEvents = favoriteEventsUi), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Shows empty state when no favorite events are found`() = runTest {
        whenever(getFavoriteAstronomicEventsUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = FavoritesViewModel(screenActions, getFavoriteAstronomicEventsUseCase)

        viewModel.uiState.test {
            assertEquals(UiState(loading = true, favoriteEvents = emptyList()), awaitItem())
            assertEquals(UiState(loading = false, favoriteEvents = emptyList()), awaitItem())
            cancel()
        }
    }
}
