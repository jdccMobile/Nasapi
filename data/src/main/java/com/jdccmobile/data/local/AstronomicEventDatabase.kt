package com.jdccmobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AstronomicEventDb::class],
    version = 1,
    exportSchema = false,
)
abstract class AstronomicEventDatabase : RoomDatabase() {
    abstract fun getAstronomicEventDao(): AstronomicEventDao
}