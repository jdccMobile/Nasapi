package com.jdccmobile.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.AstronomicEventPhoto
import com.jdccmobile.domain.model.AstronomicEventPhotoId
import java.time.LocalDate




@Entity(tableName = ASTRONOMIC_EVENT_PHOTOS_TABLE)

data class AstronomicEventPhotoDb(
    @PrimaryKey val photoId: String, // ID único para cada foto
    @ColumnInfo(name = "event_id") val eventId: String, // Relación con el evento astronómico
    @ColumnInfo(name = "file_path") val filePath: String, // Ruta local donde se guarda la foto
)

const val ASTRONOMIC_EVENT_PHOTOS_TABLE = "astronomic_events_photos_table"

fun AstronomicEventPhoto.toDb(): AstronomicEventPhotoDb =
    AstronomicEventPhotoDb(
        photoId = photoId.value,
        eventId = eventId.value,
        filePath = filePath
    )

fun AstronomicEventPhotoDb.toDomain(): AstronomicEventPhoto =
    AstronomicEventPhoto(
        photoId = AstronomicEventPhotoId(photoId),
        eventId = AstronomicEventId(eventId),
        filePath = filePath
    )
