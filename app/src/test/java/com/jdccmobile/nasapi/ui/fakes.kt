package com.jdccmobile.nasapi.ui

import arrow.core.Either
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import com.jdccmobile.domain.model.MyError
import com.jdccmobile.domain.repository.AstronomicEventPhotoRepository
import com.jdccmobile.domain.repository.AstronomicEventRepository
import com.jdccmobile.nasapi.ui.model.AstronomicEventPhotoUi
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import java.time.LocalDate

private val defaultFakeFavoriteEvents = listOf(
    AstronomicEvent(
        id = AstronomicEventId("1"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    ),
    AstronomicEvent(
        id = AstronomicEventId("2"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    ),
)

private val defaultFakeUserPhotoUiMock =
    AstronomicEventPhotoUi(
        photoId = AstronomicEventPhotoId("1"),
        eventId = AstronomicEventId("1"),
        filePath = "path",
    )

private val defaultFakeUserPhotosUiMock = List(3) {
    AstronomicEventPhotoUi(
        photoId = AstronomicEventPhotoId(it.toString()),
        eventId = AstronomicEventId(it.toString()),
        filePath = "path $it",
    )
}

private val defaultFakeFavoriteEventsUi = listOf(
    AstronomicEventUi(
        id = AstronomicEventId("1"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    ),
    AstronomicEventUi(
        id = AstronomicEventId("2"),
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    ),
)

class FakeAstronomicEventRepository : AstronomicEventRepository {
    private val favoriteEventsFlow = MutableStateFlow(defaultFakeFavoriteEvents)

    override val astronomicEvents: Flow<List<AstronomicEvent>> = flowOf(emptyList())

    override val favoriteAstronomicEvents: Flow<List<AstronomicEvent>> = favoriteEventsFlow

    override val thereIsFavEvents: Flow<Boolean> = flowOf(favoriteEventsFlow.value.isNotEmpty())
    override fun getAstronomicEventDetails(astronomicEventId: AstronomicEventId): Flow<AstronomicEvent> {
        return flowOf(favoriteEventsFlow.value.first { it.id == astronomicEventId })
    }

    override suspend fun requestAstronomicEvents(startDate: String, endDate: String): Either<MyError, Unit> = Either.Right(Unit)

    override suspend fun switchFavoriteStatus(astronomicEvent: AstronomicEvent): Either<MyError, Unit> {
        val updatedEvent = astronomicEvent.copy(isFavorite = !astronomicEvent.isFavorite)
        val updatedList = favoriteEventsFlow.value.map {
            if (it.id == astronomicEvent.id) updatedEvent else it
        }
        favoriteEventsFlow.value = updatedList
        return Either.Right(Unit)
    }
}

class FakeAstronomicEventPhotoRepository : AstronomicEventPhotoRepository {
    private val photosFlow = MutableStateFlow(defaultFakeUserPhotosUiMock.map { it.toDomain() })

    override fun photosByEvent(eventId: AstronomicEventId): Flow<List<AstronomicEventPhoto>> {
        return flowOf(photosFlow.value.filter { it.eventId == eventId })
    }

    override suspend fun insertPhoto(
        photo: AstronomicEventPhoto, file: File, imageToSave: ByteArray
    ): Either<MyError, Unit> {
        val updatedList = photosFlow.value.toMutableList().apply {
            add(photo)
        }
        photosFlow.value = updatedList
        return Either.Right(Unit)
    }

    override suspend fun deletePhoto(photo: AstronomicEventPhoto): Either<MyError, Unit> {
        val updatedList = photosFlow.value.filterNot { it.photoId == photo.photoId }
        photosFlow.value = updatedList
        return Either.Right(Unit)
    }
}


