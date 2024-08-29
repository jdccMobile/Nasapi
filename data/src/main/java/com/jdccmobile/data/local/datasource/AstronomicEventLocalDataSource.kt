package com.jdccmobile.data.local.datasource

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.local.dao.AstronomicEventDao
import com.jdccmobile.data.local.model.toDb
import com.jdccmobile.data.local.model.toDomain
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId

class AstronomicEventLocalDataSource(private val astronomicEventDao: AstronomicEventDao) {
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEvent): Either<Throwable, Unit> =
        catch { astronomicEventDao.insertAstronomicEvent(astronomicEvent.toDb()) }

    suspend fun insertAstronomicEventList(
        astronomicEventList: List<AstronomicEvent>
    ): Either<Throwable, Unit> = catch {
        astronomicEventDao.insertAstronomicEventList(astronomicEventList.map { it.toDb() })
    }

    suspend fun getAstronomicEvent(
        astronomicEventId: AstronomicEventId
    ): Either<Throwable, AstronomicEvent> = catch {
        astronomicEventDao.getAstronomicEvent(astronomicEventId.value).toDomain()
    }

    suspend fun getAstronomicEventList(
        startDate: String,
        endDate: String
    ): Either<Throwable, List<AstronomicEvent>> = catch {
        astronomicEventDao.getAstronomicEventList(startDate, endDate).map { it.toDomain() }
    }
}