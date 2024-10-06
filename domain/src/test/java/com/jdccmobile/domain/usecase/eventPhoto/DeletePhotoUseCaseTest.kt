package com.jdccmobile.domain.usecase.eventPhoto

import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeletePhotoUseCaseTest {
    private val photoMock =
        AstronomicEventPhoto(
            photoId = AstronomicEventPhotoId("photo1"),
            eventId = AstronomicEventId("1"),
            filePath = "path",
        )

    @Test
    fun `Invoke calls astronomicEventPhotoRepository`() =
        runTest {
            // Given
            val photo = photoMock
            val astronomicEventPhotoRepository = mock<AstronomicEventPhotoRepository>()
            val deletePhotoUseCase = DeletePhotoUseCase(astronomicEventPhotoRepository)

            // When
            deletePhotoUseCase(photo)

            // Then
            verify(astronomicEventPhotoRepository).deletePhoto(photo)
        }
}
