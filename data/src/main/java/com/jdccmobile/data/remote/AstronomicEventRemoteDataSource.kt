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


    suspend fun getAstronomicEventsPerWeek(
        startDate: String,
        endDate: String
    ): Either<Throwable, List<AstronomicEvent>> =
        catch {
            service.getAstronomicEventsPerWeek(apiKey, startDate, endDate).map { it.toDomain() }
        }
}


private fun AstronomicEventResult.toDomain(): AstronomicEvent = AstronomicEvent(
    title = title,
    description = explanation,
    date = LocalDate.parse(date),
    imageUrl = when (mediaType) {
        "image" -> hdUrl ?: url
        "video" -> null // TODO put gif?
        else -> null
    }
)