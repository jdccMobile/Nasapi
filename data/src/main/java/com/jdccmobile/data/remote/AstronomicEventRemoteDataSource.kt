package com.jdccmobile.data.remote

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.domain.model.AstronomicEvent
import java.time.LocalDate

class AstronomicEventRemoteDataSource(
    private val apiKey: String,
    private val service: RetrofitService
) {
    suspend fun getAstronomicEvent(): Either<Throwable, AstronomicEvent> =
        catch { service.getAstronomicEvent(apiKey).toDomain() }


//    suspend fun getCityCost(startDate: LocalDate, endDate: LocalDate): Either<Throwable, List<AstronomicEventResult>> =
//        catch { service.getAstronomicEventsPerWeek(apiKey, startDate, endDate).toDomain() }
}


private fun AstronomicEventResult.toDomain(): AstronomicEvent = AstronomicEvent(
    title = title,
    description = explanation,
    date = LocalDate.parse(date),
    imageUrl = hdUrl
)