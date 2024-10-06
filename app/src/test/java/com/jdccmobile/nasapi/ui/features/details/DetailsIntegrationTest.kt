package com.jdccmobile.nasapi.ui.features.details

import app.cash.turbine.test
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.eventPhoto.DeletePhotoUseCase
import com.jdccmobile.domain.usecase.eventPhoto.GetPhotosByEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.InsertPhotoUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventUseCase
import com.jdccmobile.domain.usecase.events.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.FakeAstronomicEventPhotoRepository
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
class DetailsIntegrationTest {
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
        val photoRepository = FakeAstronomicEventPhotoRepository()

        val vm = buildViewModelWith(repository, photoRepository)

        vm.uiState.test {
            Assert.assertTrue(awaitItem().isLoading)

            val firstEvent = repository.favoriteAstronomicEvents.first().first().toUi()
            Assert.assertEquals(firstEvent, awaitItem().astronomicEvent)

            val firstPhoto = photoRepository.photosByEvent(
                AstronomicEventId("1"),
            ).first().map { it.toUi() }
            Assert.assertEquals(firstPhoto, awaitItem().userPhotos)

            cancel()
        }
    }

    @Test
    fun `onSwitchFavStatusClicked updates favorite status`() = runTest {
        val repository = FakeAstronomicEventRepository()
        val photoRepository = FakeAstronomicEventPhotoRepository()
        val vm = buildViewModelWith(repository, photoRepository)
        val initialEvent = repository.favoriteAstronomicEvents.first().first()

        Assert.assertFalse(initialEvent.isFavorite)

        vm.onSwitchFavStatusClicked()
        runCurrent()

        val updatedEvent = repository.favoriteAstronomicEvents.first().first()
        Assert.assertTrue(updatedEvent.isFavorite)
    }

    private fun buildViewModelWith(
        repository: FakeAstronomicEventRepository,
        photoRepository: FakeAstronomicEventPhotoRepository,
        screenActions: DetailsScreenActions = defaultScreenActions(),
    ): DetailsViewModel {
        val getEventUseCase = GetAstronomicEventUseCase(repository)
        val getPhotosUseCase = GetPhotosByEventUseCase(photoRepository)
        val switchFavoriteUseCase = SwitchEventFavoriteStatusUseCase(repository)
        val insertPhotoUseCase = InsertPhotoUseCase(photoRepository)
        val deletePhotoUseCase = DeletePhotoUseCase(photoRepository)

        return DetailsViewModel(
            astronomicEventId = "1",
            screenActions = screenActions,
            getAstronomicEventUseCase = getEventUseCase,
            getPhotosByEventUseCase = getPhotosUseCase,
            switchEventFavoriteStatusUseCase = switchFavoriteUseCase,
            insertPhotoUseCase = insertPhotoUseCase,
            deletePhotoUseCase = deletePhotoUseCase,
        )
    }

    private fun defaultScreenActions() = DetailsScreenActions(
        onNavBack = {},
    )
}
