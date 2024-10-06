package com.jdccmobile.data.repository

import arrow.core.Either
import com.jdccmobile.data.local.datasource.AstronomicEventPhotoLocalDataSource
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.File

class AstronomicEventPhotoRepositoryImplTest {

    private val localDataSource: AstronomicEventPhotoLocalDataSource = mock()

    private lateinit var repository: AstronomicEventPhotoRepositoryImpl

    private val eventIdMock = AstronomicEventId("photo0")

    private val photoMock = AstronomicEventPhoto(
        photoId = AstronomicEventPhotoId("photo1"),
        eventId = AstronomicEventId("1"),
        filePath = "path"
    )

    private val photoListMock = List(3){
        AstronomicEventPhoto(
            photoId = AstronomicEventPhotoId("photo$it"),
            eventId = AstronomicEventId(it.toString()),
            filePath = "path$it"
        )
    }

    @Before
    fun setUp() {
        repository = AstronomicEventPhotoRepositoryImpl(localDataSource)
    }

    @Test
    fun `photosByEvent should return list of photos`() = runTest {
        // Given
        val eventId = eventIdMock
        val photoList = photoListMock

        whenever(localDataSource.getPhotosByEvent(eventId)).thenReturn(flowOf(photoList))

        // When
        val result = repository.photosByEvent(eventId).first()

        // Then
        assertEquals(photoList, result)
    }

    @Test
    fun `insertPhoto should return success when localDataSource insert succeeds`() = runTest {
        // Given
        val photo = photoMock
        val file = mock<File>()
        val imageToSave = ByteArray(0)

        whenever(localDataSource.insertPhoto(photo, file, imageToSave)).thenReturn(Either.Right(Unit))

        // When
        val result = repository.insertPhoto(photo, file, imageToSave)

        // Then
        assertEquals(Either.Right(Unit), result)
    }

    @Test
    fun `deletePhoto should return success when localDataSource delete succeeds`() = runTest {
        // Given
        val photo = photoMock

        whenever(localDataSource.deletePhoto(photo)).thenReturn(Either.Right(Unit))

        // When
        val result = repository.deletePhoto(photo)

        // Then
        assertEquals(Either.Right(Unit), result)
    }
}
