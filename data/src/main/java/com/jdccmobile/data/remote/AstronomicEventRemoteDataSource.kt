package com.jdccmobile.data.remote

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.utils.toMyError
import com.jdccmobile.domain.model.AstronomicEvent
import com.jdccmobile.domain.model.MyError
import java.time.LocalDate

class AstronomicEventRemoteDataSource(
    private val apiKey: String,
    private val service: RetrofitService,
) {
    suspend fun getAstronomicEvent(): Either<MyError, AstronomicEvent> =
        catch { service.getAstronomicEvent(apiKey).toDomain() }
            .mapLeft {
                println("asd1: $it")
                it.toMyError()
            }

    suspend fun getAstronomicEventsPerWeek(
        startDate: String,
        endDate: String,
    ): Either<MyError, List<AstronomicEvent>> =
        catch {
            service.getAstronomicEventsPerWeek(apiKey, startDate, endDate).map { it.toDomain() }
        }.mapLeft {
            println("asd2: $it")
            it.toMyError()
        }
}

private fun AstronomicEventResult.toDomain(): AstronomicEvent =
    AstronomicEvent(
        title = title,
        description = explanation,
        date = LocalDate.parse(date),
        imageUrl =
            when (mediaType) {
                "image" -> hdUrl ?: url
                "video" -> null // TODO put gif?
                else -> null
            },
    )
