package com.jdccmobile.domain.usecase.events

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class SwitchEventFavoriteStatusUseCaseTest {
    private val eventMock =
        AstronomicEvent(
            id = AstronomicEventId("1"),
            title = "Prueba",
            description = "Descripcion",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        )

    @Test
    fun `Invoke calls astronomicEventRepository with correct astronomicEvent`() =
        runTest {
            // Given
            val astronomicEventRepository = mock<AstronomicEventRepository>()
            val switchEventFavoriteStatusUseCase = SwitchEventFavoriteStatusUseCase(astronomicEventRepository)
            val astronomicEvent = eventMock

            // Mockeamos el comportamiento del repositorio
            whenever(astronomicEventRepository.switchFavoriteStatus(astronomicEvent))
                .thenReturn(Either.Right(Unit))

            // When
            switchEventFavoriteStatusUseCase(astronomicEvent)

            // Then
            verify(astronomicEventRepository).switchFavoriteStatus(astronomicEvent)
        }
}
