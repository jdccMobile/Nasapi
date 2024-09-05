package com.jdccmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdccmobile.data.local.model.AstronomicEventDb
import kotlinx.coroutines.flow.Flow

@Dao
interface AstronomicEventDao {
    @Query("SELECT * FROM astronomic_events_table ORDER BY date DESC")
    fun getAllAstronomicEventList(
    ): Flow<List<AstronomicEventDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEventDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstronomicEventList(astronomicEventList: List<AstronomicEventDb>)

    @Query("SELECT * FROM astronomic_events_table WHERE id = :astronomicEventId")
    suspend fun getAstronomicEvent(astronomicEventId: String): AstronomicEventDb

    @Query("SELECT * FROM astronomic_events_table WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getAstronomicEventList(
        startDate: String,
        endDate: String,
    ): List<AstronomicEventDb>

    @Query("SELECT * FROM astronomic_events_table WHERE is_favorite = true ORDER BY date ASC")
    fun getFavoriteAstronomicEventList(): Flow<List<AstronomicEventDb>>

    @Query("SELECT COUNT(id) FROM astronomic_events_table WHERE date >= :startDate AND date <= :endDate")
    suspend fun countEventsInWeek(
        startDate: String,
        endDate: String,
    ): Int

    @Query("SELECT COUNT(id) = 1 FROM astronomic_events_table WHERE date = :date")
    suspend fun hasEventForDate(date: String): Boolean
}
