package com.jdccmobile.domain.usecase.eventPhoto

import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GetPhotosByEventUseCaseTest {
    private val eventIdMock = AstronomicEventId("1")

    @Test
    fun `Invoke calls astronomicEventPhotoRepository`() =
        runTest {
            // Given
            val eventId = eventIdMock
            val astronomicEventPhotoRepository = mock<AstronomicEventPhotoRepository>()
            val getPhotosByEventUseCase = GetPhotosByEventUseCase(astronomicEventPhotoRepository)

            // When
            getPhotosByEventUseCase(eventId)

            // Then
            verify(astronomicEventPhotoRepository).photosByEvent(eventId)
        }
}
