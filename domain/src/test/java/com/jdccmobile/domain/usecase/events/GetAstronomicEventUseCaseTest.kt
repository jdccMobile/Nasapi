package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GetAstronomicEventUseCaseTest {
    private val eventIdMock = AstronomicEventId("1")

    @Test
    fun `Invoke calls astronomicEventRepository`() =
        runTest {
            // Given
            val astronomicEventId = eventIdMock
            val astronomicEventRepository = mock<AstronomicEventRepository>()
            val getAstronomicEventUseCase = GetAstronomicEventUseCase(astronomicEventRepository)

            // When
            getAstronomicEventUseCase(astronomicEventId)

            // Then
            verify(astronomicEventRepository).getAstronomicEventDetails(astronomicEventId)
        }
}
