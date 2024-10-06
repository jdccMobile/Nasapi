package com.jdccmobile.nasapi.ui.features.home

import com.jdccmobile.domain.usecase.events.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetIfThereIsFavEventsUseCase
import com.jdccmobile.domain.usecase.events.RequestAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.FakeAstronomicEventRepository
import com.jdccmobile.nasapi.ui.model.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeIntegrationTests {
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
    fun `data is loaded from repository when view model is initialized without turbine`() = runTest {
        val repository = FakeAstronomicEventRepository()
        val vm = buildViewModelWith(repository)

        val initialState = vm.uiState.value
        println("Initial State: $initialState")
        Assert.assertTrue(initialState.isInitialDataLoading)

        runCurrent()

        val expectedEvents = repository.astronomicEvents.first().map { it.toUi() }.toSet()
        println("Expected Events: $expectedEvents")

        runCurrent()

        val finalState = vm.uiState.value
        println("Final State: $finalState")

        Assert.assertFalse(finalState.isInitialDataLoading)

        Assert.assertEquals(expectedEvents, finalState.astronomicEvents)
    }

    /*
    The test with turbine does not work

    @Test
    fun `data is loaded from repository when view model is initialized`() = runTest {
        val repository = FakeAstronomicEventRepository()
        val vm = buildViewModelWith(repository)

        vm.uiState.test {
            val initialState = awaitItem()
            println("Initial State: $initialState")
            Assert.assertTrue(initialState.isInitialDataLoading)

            runCurrent()

            val expectedEvents = repository.astronomicEvents.first().map { it.toUi() }.toSet()
            println("Expected Events: $expectedEvents")

            runCurrent()

            val nextState = awaitItem()
            println("Next State: $nextState")

            Assert.assertFalse(nextState.isInitialDataLoading)
            Assert.assertEquals(expectedEvents, nextState.astronomicEvents)

            cancel()
        }
    }

*/

    @Test
    fun `onLoadMoreItems loads more events`() = runTest {
        val repository = FakeAstronomicEventRepository()

        val vm = buildViewModelWith(repository)

        vm.onLoadMoreItems()
        runCurrent()

        // Verificar que el estado de carga adicional se ha desactivado tras cargar más datos
        Assert.assertFalse(vm.uiState.value.isMoreDataLoading)
    }

    @Test
    fun `onAstronomicEventClicked triggers navigation to details`() = runTest {
        var navigatedToEventId: String? = null
        val screenActions = HomeScreenActions(
            navigateToFavorites = {},
            navigateToDetails = { eventId -> navigatedToEventId = eventId }
        )

        val repository = FakeAstronomicEventRepository()
        val vm = buildViewModelWith(repository, screenActions)

        val testEventId = repository.astronomicEvents.first().first().id.value
        vm.onAstronomicEventClicked(testEventId)

        // Verificar que se navegó al evento correcto
        Assert.assertEquals(testEventId, navigatedToEventId)
    }

    @Test
    fun `onFavoritesClicked triggers navigation to favorites`() = runTest {
        var favoritesClicked = false
        val screenActions = HomeScreenActions(
            navigateToFavorites = { favoritesClicked = true },
            navigateToDetails = {}
        )

        val repository = FakeAstronomicEventRepository()
        val vm = buildViewModelWith(repository, screenActions)

        vm.onFavoritesClicked()

        // Verificar que se ha activado la navegación a favoritos
        Assert.assertTrue(favoritesClicked)
    }

    private fun buildViewModelWith(
        repository: FakeAstronomicEventRepository,
        screenActions: HomeScreenActions = defaultScreenActions()
    ): HomeViewModel {
        val getEventsUseCase = GetAstronomicEventsUseCase(repository)
        val getFavEventsUseCase = GetIfThereIsFavEventsUseCase(repository)
        val requestEventsUseCase = RequestAstronomicEventsUseCase(repository)

        return HomeViewModel(
            screenActions = screenActions,
            requestAstronomicEventsUseCase = requestEventsUseCase,
            getAstronomicEventsUseCase = getEventsUseCase,
            getIfThereIsFavEventsUseCase = getFavEventsUseCase
        )
    }

    private fun defaultScreenActions() = HomeScreenActions(
        navigateToFavorites = {},
        navigateToDetails = {}
    )
}