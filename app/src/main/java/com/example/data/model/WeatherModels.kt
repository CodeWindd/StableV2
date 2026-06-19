package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherData(
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val locationName: String
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    val temperature: Double,
    val weatherCode: Int,
    val description: String,
    val isDay: Boolean
)

@JsonClass(generateAdapter = true)
data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val weatherCode: Int
)

@JsonClass(generateAdapter = true)
data class DailyForecast(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val weatherCode: Int
)

// Open-Meteo Models
@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    val hourly: OpenMeteoHourly?,
    val daily: OpenMeteoDaily?
)

@JsonClass(generateAdapter = true)
data class OpenMeteoHourly(
    val time: List<String>,
    @Json(name = "temperature_2m") val temperature2m: List<Double>,
    @Json(name = "weather_code") val weatherCode: List<Int>
)

@JsonClass(generateAdapter = true)
data class OpenMeteoDaily(
    val time: List<String>,
    @Json(name = "weather_code") val weatherCode: List<Int>,
    @Json(name = "temperature_2m_max") val temperatureMax: List<Double>,
    @Json(name = "temperature_2m_min") val temperatureMin: List<Double>
)

// NWS Models
@JsonClass(generateAdapter = true)
data class NwsPointsResponse(
    val properties: NwsPointsProperties
)

@JsonClass(generateAdapter = true)
data class NwsPointsProperties(
    val forecast: String,
    val forecastHourly: String,
    val relativeLocation: NwsRelativeLocation?
)

@JsonClass(generateAdapter = true)
data class NwsRelativeLocation(
    val properties: NwsRelativeLocationProperties
)

@JsonClass(generateAdapter = true)
data class NwsRelativeLocationProperties(
    val city: String,
    val state: String
)

@JsonClass(generateAdapter = true)
data class NwsForecastResponse(
    val properties: NwsForecastProperties
)

@JsonClass(generateAdapter = true)
data class NwsForecastProperties(
    val periods: List<NwsForecastPeriod>
)

@JsonClass(generateAdapter = true)
data class NwsForecastPeriod(
    val name: String?,
    val startTime: String,
    val temperature: Double,
    val shortForecast: String,
    val isDaytime: Boolean
)
