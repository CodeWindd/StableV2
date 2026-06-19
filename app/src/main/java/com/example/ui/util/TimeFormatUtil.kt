package com.example.ui.util

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object TimeFormatUtil {
    private val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
    private val outputFormatter = DateTimeFormatter.ofPattern("h a") // e.g., 2 PM
    private val dayFormatter = DateTimeFormatter.ofPattern("EEEE") // e.g., Wednesday

    fun formatToAmPm(timeString: String): String {
        return try {
            // Check if it contains 'T' which ISO usually has
            if (timeString.contains("T")) {
                val dateTime = try {
                    ZonedDateTime.parse(timeString).toLocalDateTime()
                } catch (e: Exception) {
                    LocalDateTime.parse(timeString)
                }
                dateTime.format(outputFormatter)
            } else {
                timeString // Return as is if already formatted or unknown
            }
        } catch (e: Exception) {
            timeString
        }
    }

    fun formatToDay(dateString: String): String {
        return try {
            if (dateString.contains("-")) {
                val date = LocalDateTime.parse(dateString + "T00:00").toLocalDate()
                date.format(dayFormatter)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
    
    fun getCurrentTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm a"))
    }
}
