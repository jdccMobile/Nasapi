package com.jdccmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdccmobile.data.local.model.AstronomicEventDb

@Dao
interface AstronomicEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEventDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstronomicEventList(astronomicEventList: List<AstronomicEventDb>)

    @Query("SELECT * FROM astronomic_events_table WHERE id = :astronomicEventId")
    suspend fun getAstronomicEvent(astronomicEventId: String): AstronomicEventDb

    @Query("SELECT * FROM astronomic_events_table WHERE date >= :startDate AND date <= :endDate")
    suspend fun getAstronomicEventList(startDate: String, endDate: String): List<AstronomicEventDb>

    @Query("SELECT COUNT(id) FROM astronomic_events_table WHERE date >= :startDate AND date <= :endDate")
    suspend fun countEventsInWeek(startDate: String, endDate: String): Int
}