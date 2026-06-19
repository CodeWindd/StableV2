package com.example.data.api

import com.example.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,weather_code",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("temperature_unit") tempUnit: String = "fahrenheit",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoResponse
}

interface NwsApi {
    @GET("points/{lat},{lon}")
    suspend fun getPoints(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double
    ): NwsPointsResponse

    @GET
    suspend fun getForecast(@Url url: String): NwsForecastResponse
}
