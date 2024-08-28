package com.jdccmobile.data.remote

import com.jdccmobile.data.remote.model.AstronomicEventResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("planetary/apod")
    suspend fun getAstronomicEvent(
        @Query("api_key") apiKey: String,
    ): AstronomicEventResponse

    @GET("planetary/apod")
    suspend fun getAstronomicEventsPerWeek(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): List<AstronomicEventResponse>
}

object RetrofitServiceFactory {
    private const val BASE_URL = "https://api.nasa.gov/"

    fun makeRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}
