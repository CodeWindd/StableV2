package com.example.ui.util

object WeatherIconMapper {
    private const val BASE_URL = "https://cdn.meteocons.com/3.0.0-next.10/svg/fill/"

    fun getIconUrl(code: Int, isDay: Boolean): String {
        return when (code) {
            0 -> if (isDay) "${BASE_URL}clear-day.svg" else "${BASE_URL}clear-night.svg"
            1, 2 -> if (isDay) "${BASE_URL}partly-cloudy-day.svg" else "${BASE_URL}partly-cloudy-night.svg"
            3 -> "${BASE_URL}overcast.svg"
            45, 48 -> "${BASE_URL}fog.svg"
            51, 53, 55 -> "${BASE_URL}drizzle.svg"
            61, 63 -> "${BASE_URL}rain.svg"
            65 -> "${BASE_URL}extreme-rain.svg"
            71, 73, 75 -> "${BASE_URL}snow.svg"
            77 -> "${BASE_URL}snowflake.svg"
            80, 81, 82 -> "${BASE_URL}raindrops.svg"
            85, 86 -> "${BASE_URL}cloudy-day-snow.svg"
            95, 96, 99 -> "${BASE_URL}thunderstorms.svg"
            else -> "${BASE_URL}not-available.svg"
        }
    }

    // Mapping NWS descriptions to URLs if code is 0 (as NWS doesn't always provide simple codes)
    fun getIconUrlFromDescription(description: String, isDay: Boolean): String {
        val desc = description.lowercase()
        return when {
            desc.contains("clear") || desc.contains("sunny") -> if (isDay) "${BASE_URL}clear-day.svg" else "${BASE_URL}clear-night.svg"
            desc.contains("partly cloudy") || desc.contains("mostly clear") -> if (isDay) "${BASE_URL}partly-cloudy-day.svg" else "${BASE_URL}partly-cloudy-night.svg"
            desc.contains("mostly cloudy") || desc.contains("cloudy") -> "${BASE_URL}cloudy.svg"
            desc.contains("overcast") -> "${BASE_URL}overcast.svg"
            desc.contains("fog") || desc.contains("haze") || desc.contains("mist") -> "${BASE_URL}fog.svg"
            desc.contains("drizzle") -> "${BASE_URL}drizzle.svg"
            desc.contains("rain") || desc.contains("showers") -> "${BASE_URL}rain.svg"
            desc.contains("snow") -> "${BASE_URL}snow.svg"
            desc.contains("thunderstorm") -> "${BASE_URL}thunderstorms.svg"
            desc.contains("wind") -> "${BASE_URL}wind.svg"
            desc.contains("tornado") -> "${BASE_URL}tornado.svg"
            else -> "${BASE_URL}not-available.svg"
        }
    }
}
