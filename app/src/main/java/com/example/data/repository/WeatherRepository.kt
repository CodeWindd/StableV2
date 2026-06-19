package com.example.data.repository

import com.example.data.api.NwsApi
import com.example.data.api.OpenMeteoApi
import com.example.data.model.*
import java.util.*

class WeatherRepository(
    private val nwsApi: NwsApi,
    private val openMeteoApi: OpenMeteoApi
) {
    suspend fun getWeatherData(lat: Double, lon: Double): WeatherData {
        return try {
            // Try NWS first for US locations
            if (isInUsRange(lat, lon)) {
                getNwsWeatherData(lat, lon)
            } else {
                getOpenMeteoWeatherData(lat, lon)
            }
        } catch (e: Exception) {
            // Fallback to OpenMeteo if NWS fails or is unavailable
            getOpenMeteoWeatherData(lat, lon)
        }
    }

    private fun isInUsRange(lat: Double, lon: Double): Boolean {
        // Rough bounding box for US (including Alaska and Hawaii)
        return lat in 18.0..72.0 && lon in -170.0..-66.0
    }

    private suspend fun getNwsWeatherData(lat: Double, lon: Double): WeatherData {
        val points = nwsApi.getPoints(lat, lon)
        val forecast = nwsApi.getForecast(points.properties.forecast)
        val hourlyForecast = nwsApi.getForecast(points.properties.forecastHourly)

        val locationName = points.properties.relativeLocation?.properties?.let {
            "${it.city}, ${it.state}"
        } ?: "Unknown Location"

        val currentPeriod = forecast.properties.periods.first()
        
        return WeatherData(
            current = CurrentWeather(
                temperature = currentPeriod.temperature,
                weatherCode = 0, // NWS uses shortForecast string
                description = currentPeriod.shortForecast,
                isDay = currentPeriod.isDaytime
            ),
            hourly = hourlyForecast.properties.periods.take(24).map {
                HourlyForecast(
                    time = it.startTime,
                    temperature = it.temperature,
                    weatherCode = 0
                )
            },
            daily = forecast.properties.periods.filter { it.name?.contains("Night") == false }.map {
                DailyForecast(
                    date = it.name ?: it.startTime,
                    maxTemp = it.temperature,
                    minTemp = it.temperature - 10, // NWS daily forecast is split into day/night
                    weatherCode = 0
                )
            },
            locationName = locationName
        )
    }

    private suspend fun getOpenMeteoWeatherData(lat: Double, lon: Double): WeatherData {
        val response = openMeteoApi.getForecast(lat, lon)
        
        val hourly = response.hourly!!
        val daily = response.daily!!
        
        val currentTemp = hourly.temperature2m.first()
        val currentCode = hourly.weatherCode.first()
        
        return WeatherData(
            current = CurrentWeather(
                temperature = currentTemp,
                weatherCode = currentCode,
                description = getWeatherDescription(currentCode),
                isDay = true // Simplified
            ),
            hourly = hourly.time.zip(hourly.temperature2m).zip(hourly.weatherCode).take(24).map { (data, code) ->
                val (time, temp) = data
                HourlyForecast(time, temp, code)
            },
            daily = daily.time.indices.map { i ->
                DailyForecast(
                    date = daily.time[i],
                    maxTemp = daily.temperatureMax[i],
                    minTemp = daily.temperatureMin[i],
                    weatherCode = daily.weatherCode[i]
                )
            },
            locationName = "Lat: %.2f, Lon: %.2f".format(lat, lon)
        )
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
            45, 48 -> "Fog and depositing rime fog"
            51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
            56, 57 -> "Freezing Drizzle: Light and dense intensity"
            61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
            66, 67 -> "Freezing Rain: Light and heavy intensity"
            71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
            85, 86 -> "Snow showers slight and heavy"
            95 -> "Thunderstorm: Slight or moderate"
            96, 99 -> "Thunderstorm with slight and heavy hail"
            else -> "Unknown"
        }
    }
}
