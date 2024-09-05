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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class AstronomicEventLocalDataSource(private val astronomicEventDao: AstronomicEventDao) {
    fun getAllAstronomicEventList(
    ): Flow<List<AstronomicEvent>> =
        astronomicEventDao.getAllAstronomicEventList()
            .mapLatest { events -> events.map { it.toDomain() } }

    suspend fun insertAstronomicEvent(astronomicEvent: AstronomicEvent): Either<MyError, Unit> =
        catch {
            astronomicEventDao.insertAstronomicEvent(astronomicEvent.toDb())
        }.mapLeft { it.toMyError() }

    suspend fun insertAstronomicEventList(astronomicEventList: List<AstronomicEvent>): Either<MyError, Unit> =
        catch {
            astronomicEventDao.insertAstronomicEventList(astronomicEventList.map { it.toDb() })
        }.mapLeft { it.toMyError() }

    suspend fun getAstronomicEvent(astronomicEventId: AstronomicEventId): Either<MyError, AstronomicEvent> =
        catch {
            astronomicEventDao.getAstronomicEvent(astronomicEventId.value).toDomain()
        }.mapLeft { it.toMyError() }

    suspend fun getAstronomicEventList(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        catch {
            astronomicEventDao.getAstronomicEventList(startDate, endDate).map { it.toDomain() }
        }.mapLeft { it.toMyError() }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFavoriteAstronomicEventList(): Flow<List<AstronomicEvent>> =
        astronomicEventDao.getFavoriteAstronomicEventList().mapLatest { events ->
            events.map { event ->
                event.toDomain()
            }
        }

    suspend fun countEventsInWeek(
        startDate: String,
        endDate: String,
    ): Either<MyError, Int> =
        catch {
            astronomicEventDao.countEventsInWeek(startDate, endDate)
        }.mapLeft { it.toMyError() }

    suspend fun hasEventForDate(date: String): Either<MyError, Boolean> =
        catch {
            astronomicEventDao.hasEventForDate(date)
        }.mapLeft { it.toMyError() }
}
