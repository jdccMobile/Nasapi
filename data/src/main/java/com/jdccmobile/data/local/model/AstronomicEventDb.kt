package com.jdccmobile.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import java.time.LocalDate

@Entity(tableName = ASTRONOMIC_EVENTS_TABLE)
data class AstronomicEventDb(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "has_image") val hasImage: Boolean,
)

const val ASTRONOMIC_EVENTS_TABLE = "astronomic_events_table"

fun AstronomicEvent.toDb(): AstronomicEventDb =
    AstronomicEventDb(
        id = id.value,
        title = title,
        description = description,
        date = date.toString(),
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        hasImage = hasImage,
    )

fun AstronomicEventDb.toDomain(): AstronomicEvent =
    AstronomicEvent(
        id = AstronomicEventId(id),
        title = title,
        description = description,
        date = LocalDate.parse(date),
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        hasImage = hasImage,
    )
