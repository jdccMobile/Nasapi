package com.jdccmobile.domain.usecase.eventPhoto

import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.File

class InsertPhotoUseCaseTest {
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
            val file = File("test.jpg")
            val imageToSave = ByteArray(10)
            val astronomicEventPhotoRepository = mock<AstronomicEventPhotoRepository>()
            val insertPhotoUseCase = InsertPhotoUseCase(astronomicEventPhotoRepository)

            // When
            insertPhotoUseCase(photo, file, imageToSave)

            // Then
            verify(astronomicEventPhotoRepository).insertPhoto(photo, file, imageToSave)
        }
}
