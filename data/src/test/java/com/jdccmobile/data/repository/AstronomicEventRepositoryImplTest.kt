package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventLocalDataSource
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class AstronomicEventRepositoryImplTest {

//    private val astronomicEventDao: AstronomicEventDao = mock()

    private val requestAndInsertEventsPerWeek: RequestAndInsertEventsPerWeek = mock()

    private var localDataSource: AstronomicEventLocalDataSource = mock()

    private lateinit var repository: AstronomicEventRepositoryImpl

    private val eventIdMock = AstronomicEventId("1")

    private val eventMock = AstronomicEvent(
        id = AstronomicEventId("1"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    )

    private val eventListMock = List(7) {
        AstronomicEvent(
            id = AstronomicEventId(it.toString()),
            title = "Prueba $it",
            description = "Descripcion $it",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        )
    }

    @Before
    fun setUp() {
        whenever(localDataSource.astronomicEvents()).thenReturn(flowOf(eventListMock) )
        repository = AstronomicEventRepositoryImpl(localDataSource, requestAndInsertEventsPerWeek)
    }

    @Test
    fun `astronomicEvents should return list of events`() = runTest {
        // When
        val result = repository.astronomicEvents.firstOrNull()

        // Then
        assertEquals(eventListMock, result)
    }



    @Test
    fun `getAstronomicEventDetails should return event details`() = runTest {
        // Given
        val eventId = eventIdMock
        val event = eventMock

        whenever(localDataSource.getAstronomicEvent(eventId)).thenReturn(flowOf(event))

        // When
        val result = repository.getAstronomicEventDetails(eventId).first()
        println("result:" + result)

        // Then
        assertEquals(event, result)
    }

    @Test
    fun `requestAstronomicEvents should return success when events in week match`() = runTest {
        // Given
        val startDate = "2023-01-01"
        val endDate = "2023-01-07"
        val events = List(EVENTS_IN_WEEK) {
            AstronomicEvent(
                id = AstronomicEventId(it.toString()),
                title = "Prueba $it",
                description = "Descripcion $it",
                date = LocalDate.now(),
                imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                isFavorite = false,
                hasImage = false,
            )
        }

        whenever(
            localDataSource.getAstronomicEventList(
                startDate,
                endDate
            )
        ).thenReturn(Either.Right(events))

        // When
        val result = repository.requestAstronomicEvents(startDate, endDate)

        // Then
        assertEquals(Either.Right(Unit), result)
    }


    @Test
    fun `requestAstronomicEvents should request all events when no events in week`() = runTest {
        // Given
        val startDate = "2023-01-01"
        val endDate = "2023-01-07"

        whenever(
            localDataSource.getAstronomicEventList(
                startDate,
                endDate
            )
        ).thenReturn(Either.Right(emptyList()))

        whenever(
            requestAndInsertEventsPerWeek.allEvents(
                startDate,
                endDate
            )
        ).thenReturn(Either.Right(Unit))

        // When
        val result = repository.requestAstronomicEvents(startDate, endDate)

        // Then
        assertEquals(Either.Right(Unit), result)
    }

    @Test
    fun `requestAstronomicEvents should request specific events when events in week are intermediate`() =
        runTest {
            // Given
            val startDate = "2023-01-01"
            val endDate = "2023-01-07"
            val events = List(3) {
                AstronomicEvent(
                    id = AstronomicEventId(it.toString()),
                    title = "Prueba $it",
                    description = "Descripcion $it",
                    date = LocalDate.now(),
                    imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                    isFavorite = false,
                    hasImage = false,
                )
            }

            whenever(
                localDataSource.getAstronomicEventList(
                    startDate,
                    endDate
                )
            ).thenReturn(Either.Right(events))

            whenever(requestAndInsertEventsPerWeek.specificEvents(startDate, endDate)).thenReturn(
                Either.Right(Unit)
            )

            // When
            val result = repository.requestAstronomicEvents(startDate, endDate)

            // Then
            assertEquals(Either.Right(Unit), result)
        }

    @Test
    fun `switchFavoriteStatus should return success when switching favorite status`() = runTest {
        // Given
        val event = eventMock

        whenever(localDataSource.switchFavoriteStatus(event)).thenReturn(Either.Right(Unit))

        // When
        val result = repository.switchFavoriteStatus(event)

        // Then
        assertEquals(Either.Right(Unit), result)
    }
}