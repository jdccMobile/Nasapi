package com.jdccmobile.data.local

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId

class AstronomicEventLocalDataSource(private val astronomicEventDao: AstronomicEventDao) {
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEvent): Either<Throwable, Unit> =
        catch { astronomicEventDao.insertAstronomicEvent(astronomicEvent.toDb()) }

    suspend fun getAstronomicEvent(astronomicEventId: AstronomicEventId): Either<Throwable, AstronomicEvent> =
        catch { astronomicEventDao.getAstronomicEvent(astronomicEventId.value).toDomain() }
}