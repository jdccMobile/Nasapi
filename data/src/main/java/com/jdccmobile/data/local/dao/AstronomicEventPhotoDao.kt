package com.jdccmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdccmobile.data.local.model.AstronomicEventDb
import com.jdccmobile.data.local.model.AstronomicEventPhotoDb
import kotlinx.coroutines.flow.Flow

@Dao
interface AstronomicEventPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: AstronomicEventPhotoDb)

    @Query("SELECT * FROM astronomic_events_photos_table WHERE event_id = :eventId")
    fun getPhotosByEvent(eventId: String): Flow<List<AstronomicEventPhotoDb>>
}

