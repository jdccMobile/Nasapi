package com.jdccmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AstronomicEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEventDb)

    @Query("SELECT * FROM astronomic_events_table WHERE id = :astronomicEventId")
    suspend fun getAstronomicEvent(astronomicEventId: String): AstronomicEventDb

}