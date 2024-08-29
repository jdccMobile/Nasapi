package com.jdccmobile.data.local.datasource

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.local.dao.AstronomicEventDao
import com.jdccmobile.data.local.model.toDb
import com.jdccmobile.data.local.model.toDomain
import com.jdccmobile.data.utils.toMyError
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.domain.model.MyError

class AstronomicEventLocalDataSource(private val astronomicEventDao: AstronomicEventDao) {
    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEvent): Either<MyError, Unit> =
        catch {
            astronomicEventDao.insertAstronomicEvent(astronomicEvent.toDb())
        }.mapLeft { it.toMyError() }

    suspend fun insertAstronomicEventList(
        astronomicEventList: List<AstronomicEvent>
    ): Either<MyError, Unit> = catch {
        astronomicEventDao.insertAstronomicEventList(astronomicEventList.map { it.toDb() })
    }.mapLeft { it.toMyError() }

    suspend fun getAstronomicEvent(
        astronomicEventId: AstronomicEventId
    ): Either<MyError, AstronomicEvent> = catch {
        astronomicEventDao.getAstronomicEvent(astronomicEventId.value).toDomain()
    }.mapLeft { it.toMyError() }

    suspend fun getAstronomicEventList(
        startDate: String,
        endDate: String
    ): Either<MyError, List<AstronomicEvent>> = catch {
        astronomicEventDao.getAstronomicEventList(startDate, endDate).map { it.toDomain() }
    }.mapLeft { it.toMyError() }

    suspend fun countEventsInWeek(
        startDate: String,
        endDate: String
    ): Either<MyError, Int> = catch {
        astronomicEventDao.countEventsInWeek(startDate, endDate)
    }.mapLeft { it.toMyError() }
}