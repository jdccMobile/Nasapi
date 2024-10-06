package com.jdccmobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdccmobile.data.local.dao.AstronomicEventDao
import com.jdccmobile.data.local.dao.AstronomicEventPhotoDao
import com.jdccmobile.data.local.model.AstronomicEventDb
import com.jdccmobile.data.local.model.AstronomicEventPhotoDb

@Database(
    entities = [AstronomicEventDb::class, AstronomicEventPhotoDb::class],
    version = 1,
    exportSchema = false,
)
abstract class AstronomicEventDatabase : RoomDatabase() {
    abstract fun getAstronomicEventDao(): AstronomicEventDao
    abstract fun getAstronomicEventPhotoDao(): AstronomicEventPhotoDao
}
