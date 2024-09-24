package com.jdccmobile.domain.usecase.events

import com.jdccmobile.domain.repository.AstronomicEventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GetFavoriteAstronomicEventsUseCaseTest {
    @Test
    fun `Invoke calls astronomicEventRepository`() =
        runTest {
            // Given
            val astronomicEventRepository = mock<AstronomicEventRepository>()
            val getFavoriteAstronomicEventsUseCase = GetFavoriteAstronomicEventsUseCase(astronomicEventRepository)

            // When
            getFavoriteAstronomicEventsUseCase()

            // Then
            verify(astronomicEventRepository).favoriteAstronomicEvents
        }
}
