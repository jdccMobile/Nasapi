package com.jdccmobile.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import java.time.LocalDate




@Entity(tableName = ASTRONOMIC_EVENT_PHOTOS_TABLE)

data class AstronomicEventPhotoDb(
    @PrimaryKey val photoId: String, // ID único para cada foto
    @ColumnInfo(name = "event_id") val eventId: String, // Relación con el evento astronómico
    @ColumnInfo(name = "file_path") val filePath: String, // Ruta local donde se guarda la foto
)

const val ASTRONOMIC_EVENT_PHOTOS_TABLE = "astronomic_events_photos_table"
