import app.cash.turbine.test
import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.usecase.events.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetIfThereIsFavEventsUseCase
import com.jdccmobile.domain.usecase.events.RequestAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.features.home.ErrorUi
import com.jdccmobile.nasapi.ui.features.home.HomeScreenActions
import com.jdccmobile.nasapi.ui.features.home.HomeViewModel
import com.jdccmobile.nasapi.ui.features.home.LoadingType
import com.jdccmobile.nasapi.ui.features.home.UiState
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.utils.toMessage
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
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    lateinit var requestAstronomicEventsUseCase: RequestAstronomicEventsUseCase

    @Mock
    lateinit var getAstronomicEventsUseCase: GetAstronomicEventsUseCase

    @Mock
    lateinit var getIfThereIsFavEventsUseCase: GetIfThereIsFavEventsUseCase

    @Mock
    lateinit var screenActions: HomeScreenActions

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val astronomicEvents = listOf(
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

    private val astronomicEventsUi = listOf(
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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getAstronomicEventsUseCase()).thenReturn(flowOf(astronomicEvents))
        whenever(getIfThereIsFavEventsUseCase()).thenReturn(flowOf(true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state is loading and events are loaded successfully`() = runTest {
        whenever(requestAstronomicEventsUseCase(any(), any())).thenReturn(Either.Right(Unit))
        viewModel = HomeViewModel(
            screenActions,
            requestAstronomicEventsUseCase,
            getAstronomicEventsUseCase,
            getIfThereIsFavEventsUseCase,
        )
        viewModel.uiState.test {
            assertEquals(
                UiState(
                    isInitialDataLoading = true,
                    isMoreDataLoading = false,
                    astronomicEvents = emptySet(),
                    thereIsFavEvents = false,
                    error = null,
                ),
                awaitItem(),
            )
        }
        runCurrent()
        viewModel.uiState.test {
            assertEquals(
                UiState(
                    isInitialDataLoading = false,
                    isMoreDataLoading = false,
                    astronomicEvents = astronomicEventsUi.toSet(),
                    thereIsFavEvents = true,
                    error = null,
                ),
                awaitItem(),
            )

            cancel()
        }
    }

    @Test
    fun `Load more items updates the UI correctly`() = runTest {
        whenever(requestAstronomicEventsUseCase(any(), any())).thenReturn(Either.Right(Unit))
        viewModel = HomeViewModel(
            screenActions,
            requestAstronomicEventsUseCase,
            getAstronomicEventsUseCase,
            getIfThereIsFavEventsUseCase,
        )
        viewModel.requestInitialEvents()
        runCurrent()
        viewModel.onLoadMoreItems()

        viewModel.uiState.test {
            assertEquals(
                UiState(
                    isInitialDataLoading = false,
                    isMoreDataLoading = true,
                    astronomicEvents = astronomicEventsUi.toSet(),
                    thereIsFavEvents = true,
                    error = null,
                ),
                awaitItem(),
            )

            assertEquals(
                UiState(
                    isInitialDataLoading = false,
                    isMoreDataLoading = false,
                    astronomicEvents = astronomicEventsUi.toSet(),
                    thereIsFavEvents = true,
                    error = null,
                ),
                awaitItem(),
            )

            cancel()
        }
    }

    @Test
    fun `Shows error when request for init events fails`() = runTest {
        whenever(
            requestAstronomicEventsUseCase(
                any(),
                any(),
            ),
        ).thenReturn(Either.Left(MyError.Connectivity))
        viewModel = HomeViewModel(
            screenActions,
            requestAstronomicEventsUseCase,
            getAstronomicEventsUseCase,
            getIfThereIsFavEventsUseCase,
        )

        viewModel.requestInitialEvents()
        runCurrent()

        viewModel.uiState.test {
            assertEquals(
                UiState(
                    isInitialDataLoading = false,
                    isMoreDataLoading = false,
                    astronomicEvents = astronomicEventsUi.toSet(),
                    thereIsFavEvents = true,
                    error = ErrorUi(MyError.Connectivity.toMessage(), LoadingType.InitialLoading),
                ),
                awaitItem(),
            )
            cancel()
        }
    }
}
