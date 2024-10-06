package com.jdccmobile.nasapi.ui.features.details

import app.cash.turbine.test
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import com.jdccmobile.domain.usecase.eventPhoto.DeletePhotoUseCase
import com.jdccmobile.domain.usecase.eventPhoto.GetPhotosByEventUseCase
import com.jdccmobile.domain.usecase.eventPhoto.InsertPhotoUseCase
import com.jdccmobile.domain.usecase.events.GetAstronomicEventUseCase
import com.jdccmobile.domain.usecase.events.SwitchEventFavoriteStatusUseCase
import com.jdccmobile.nasapi.ui.model.AstronomicEventPhotoUi
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.io.File
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest {
    @Mock
    lateinit var getAstronomicEventUseCase: GetAstronomicEventUseCase

    @Mock
    lateinit var getPhotosByEventUseCase: GetPhotosByEventUseCase

    @Mock
    lateinit var switchEventFavoriteStatusUseCase: SwitchEventFavoriteStatusUseCase

    @Mock
    lateinit var insertPhotoUseCase: InsertPhotoUseCase

    @Mock
    lateinit var deletePhotoUseCase: DeletePhotoUseCase

    @Mock
    lateinit var screenActions: DetailsScreenActions

    private lateinit var viewModel: DetailsViewModel

    private val astronomicEventId = "eventId"

    private val astronomicEventUiMock = AstronomicEventUi(
        id = AstronomicEventId("1"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    )
    private val userPhotoUiMock =
        AstronomicEventPhotoUi(
            photoId = AstronomicEventPhotoId("1"),
            eventId = AstronomicEventId("1"),
            filePath = "path",
        )

    private val userPhotosUiMock = List(3) {
        AstronomicEventPhotoUi(
            photoId = AstronomicEventPhotoId(it.toString()),
            eventId = AstronomicEventId(it.toString()),
            filePath = "path $it",
        )
    }

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(
            getAstronomicEventUseCase(any()),
        ).thenReturn(flowOf(astronomicEventUiMock.toDomain()))
        whenever(
            getPhotosByEventUseCase(any()),
        ).thenReturn(flowOf(userPhotosUiMock.map { it.toDomain() }))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Loading state is updated after astronomic event and photos are loaded`() = runTest {
        viewModel = DetailsViewModel(
            astronomicEventId,
            screenActions,
            getAstronomicEventUseCase,
            getPhotosByEventUseCase,
            switchEventFavoriteStatusUseCase,
            insertPhotoUseCase,
            deletePhotoUseCase,
        )
        viewModel.uiState.test {
            assertEquals(
                UiState(
                    isLoading = true,
                    showCameraView = false,
                    astronomicEvent = null,
                    userPhotos = emptyList(),
                ),
                awaitItem(),
            )

            assertEquals(
                UiState(
                    isLoading = false,
                    showCameraView = false,
                    astronomicEvent = astronomicEventUiMock,
                    userPhotos = emptyList(),
                ),
                awaitItem(),
            )

            assertEquals(
                UiState(
                    isLoading = false,
                    showCameraView = false,
                    astronomicEvent = astronomicEventUiMock,
                    userPhotos = userPhotosUiMock,
                ),
                awaitItem(),
            )
            cancel()
        }
    }

    @Test
    fun `Switch favorite status calls the corresponding use case`() = runTest {
        viewModel = DetailsViewModel(
            astronomicEventId,
            screenActions,
            getAstronomicEventUseCase,
            getPhotosByEventUseCase,
            switchEventFavoriteStatusUseCase,
            insertPhotoUseCase,
            deletePhotoUseCase,
        )

        viewModel.onSwitchFavStatusClicked()
        runCurrent()
        verify(switchEventFavoriteStatusUseCase).invoke(astronomicEventUiMock.toDomain())
    }

    @Test
    fun `Camera view is shown when camera button is clicked and closed when photo is saved`() =
        runTest {
            viewModel = DetailsViewModel(
                astronomicEventId,
                screenActions,
                getAstronomicEventUseCase,
                getPhotosByEventUseCase,
                switchEventFavoriteStatusUseCase,
                insertPhotoUseCase,
                deletePhotoUseCase,
            )
            viewModel.onOpenCameraClicked()
            runCurrent()
            viewModel.uiState.test {
                assertEquals(
                    UiState(
                        isLoading = false,
                        showCameraView = true,
                        astronomicEvent = astronomicEventUiMock,
                        userPhotos = userPhotosUiMock,
                    ),
                    awaitItem(),
                )
                cancel()
            }

            val photo = userPhotoUiMock
            val file = File("dummyPath")
            val imageToSave = ByteArray(0)

            viewModel.onSavePhotoTaken(photo, file, imageToSave)
            runCurrent()
            viewModel.uiState.test {
                assertEquals(
                    UiState(
                        isLoading = false,
                        showCameraView = false,
                        astronomicEvent = astronomicEventUiMock,
                        userPhotos = userPhotosUiMock,
                    ),
                    awaitItem(),
                )
                cancel()
            }
        }

    @Test
    fun `Delete photo calls deletePhotoUseCase`() = runTest {
        viewModel = DetailsViewModel(
            astronomicEventId,
            screenActions,
            getAstronomicEventUseCase,
            getPhotosByEventUseCase,
            switchEventFavoriteStatusUseCase,
            insertPhotoUseCase,
            deletePhotoUseCase,
        )

        val photo = userPhotoUiMock
        viewModel.onDeletePhoto(photo)
        runCurrent()
        verify(deletePhotoUseCase).invoke(photo.toDomain())
    }
}
