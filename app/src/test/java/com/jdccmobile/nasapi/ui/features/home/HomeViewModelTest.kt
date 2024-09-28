import app.cash.turbine.test
import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.usecase.events.GetAstronomicEventsUseCase
import com.jdccmobile.domain.usecase.events.GetIfThereIsFavEventsUseCase
import com.jdccmobile.domain.usecase.events.RequestAstronomicEventsUseCase
import com.jdccmobile.nasapi.ui.features.home.HomeScreenActions
import com.jdccmobile.nasapi.ui.features.home.HomeViewModel
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var screenActions: HomeScreenActions

    @Mock
    lateinit var requestAstronomicEventsUseCase: RequestAstronomicEventsUseCase

    @Mock
    lateinit var getAstronomicEventsUseCase: GetAstronomicEventsUseCase

    @Mock
    lateinit var getIfThereIsFavEventsUseCase: GetIfThereIsFavEventsUseCase

    private lateinit var vm: HomeViewModel

    private val eventsMock = List(5) {
        AstronomicEvent(
            id = AstronomicEventId(it.toString()),
            title = "Prueba $it",
            description = "Description $it",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        )
    }

    private val eventsUiMock = List(5) {
        AstronomicEventUi(
            id = AstronomicEventId(it.toString()),
            title = "Prueba $it",
            description = "Description $it",
            date = LocalDate.now().plusWeeks(it.toLong()),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        vm = HomeViewModel(
            screenActions,
            requestAstronomicEventsUseCase,
            getAstronomicEventsUseCase,
            getIfThereIsFavEventsUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial data is loaded correctly`() = runTest {
        Dispatchers.setMain(testDispatcher)


        // Mockear que el flujo emite los eventos
        whenever(getAstronomicEventsUseCase()).thenReturn(flow {
            emitAll(flowOf(eventsMock)) // Asegúrate de que emita todos los eventos
        })
        runCurrent()
        whenever(requestAstronomicEventsUseCase(anyString(), anyString())).thenReturn(Either.Right(Unit))

        withTimeout(2000) {
            vm.isInitialDataLoading.test {
                assert(awaitItem()) // true en la primera emisión
                vm.onLoadMoreItems()
                advanceUntilIdle()
                assert(!awaitItem()) // false en la segunda emisión
            }

            vm.astronomicEvents.test {
                assert(awaitItem().isEmpty()) // Debería estar vacío
                vm.onLoadMoreItems()
                advanceUntilIdle()
                val loadedEvents = awaitItem()
                assertEquals(eventsUiMock.toSet(), loadedEvents)
            }
        }

        Dispatchers.resetMain()
    }


}
