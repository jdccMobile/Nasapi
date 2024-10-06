package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GetAstronomicEventsUseCaseTest {
    @Test
    fun `Invoke calls astronomicEventRepository`() =
        runTest {
            // Given
            val astronomicEventRepository = mock<AstronomicEventRepository>()
            val getAstronomicEventsUseCase = GetAstronomicEventsUseCase(astronomicEventRepository)

            // When
            getAstronomicEventsUseCase()

            // Then
            verify(astronomicEventRepository).astronomicEvents
        }
}
