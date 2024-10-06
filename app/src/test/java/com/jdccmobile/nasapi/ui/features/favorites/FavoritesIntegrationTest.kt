package com.jdccmobile.nasapi.ui.features.favorites

import app.cash.turbine.test
import com.jdccmobile.domain.usecase.events.GetFavoriteAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.FakeAstronomicEventRepository
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesIntegrationTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `data is loaded from repository when view model is initialized`() = runTest {
        val repository = FakeAstronomicEventRepository()

        val vm = buildViewModelWith(repository)

        vm.uiState.test {
            Assert.assertEquals(UiState(loading = true, favoriteEvents = emptyList()), awaitItem())
            Assert.assertEquals(
                UiState(
                    loading = false,
                    favoriteEvents = repository.favoriteAstronomicEvents.first().toUi(),
                ),
                awaitItem(),
            )
            cancel()
        }
    }

    @Test
    fun `onFavoriteEventClicked triggers navigation to details`() = runTest {
        var clickedEventId: String? = null
        val actions = FavoritesScreenActions(
            onNavBack = {},
            onNavToDetails = { eventId -> clickedEventId = eventId },
        )
        val repository = FakeAstronomicEventRepository()
        val vm = buildViewModelWith(repository, actions)

        val testEventId = repository.favoriteAstronomicEvents.first().first().id.value
        vm.onFavoriteEventClicked(testEventId)

        Assert.assertEquals(testEventId, clickedEventId)
    }

    private fun buildViewModelWith(
        repository: FakeAstronomicEventRepository,
        screenActions: FavoritesScreenActions = defaultScreenActions(),
    ): FavoritesViewModel {
        val getFavoriteEventsUseCase = GetFavoriteAstronomicEventsUseCase(repository)
        return FavoritesViewModel(screenActions, getFavoriteEventsUseCase)
    }

    private fun defaultScreenActions() = FavoritesScreenActions(
        onNavBack = {},
        onNavToDetails = {},
    )
}
