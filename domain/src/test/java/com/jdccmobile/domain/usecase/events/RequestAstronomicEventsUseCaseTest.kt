package com.jdccmobile.domain.usecase.events

import arrow.core.Either
import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RequestAstronomicEventsUseCaseTest {
    @Test
    fun `Invoke calls astronomicEventRepository with correct parameters`() =
        runTest {
            // Given
            val astronomicEventRepository = mock<AstronomicEventRepository>()
            val requestAstronomicEventsUseCase = RequestAstronomicEventsUseCase(astronomicEventRepository)
            val startDate = "2023-01-01"
            val endDate = "2023-12-31"

            whenever(astronomicEventRepository.requestAstronomicEvents(startDate, endDate))
                .thenReturn(Either.Right(Unit))

            // When
            requestAstronomicEventsUseCase(startDate, endDate)

            // Then
            verify(astronomicEventRepository).requestAstronomicEvents(startDate, endDate)
        }
}
