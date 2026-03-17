package com.b7b.sobriety.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val isoFormatter = DateTimeFormatter.ISO_DATE_TIME

    fun parseDate(dateStr: String?): LocalDate? {
        if (dateStr == null) return null
        return try {
            LocalDate.parse(dateStr.split("T")[0], dateFormatter)
        } catch (e: Exception) {
            null
        }
    }

    fun parseDateTime(dateTimeStr: String?): LocalDateTime? {
        if (dateTimeStr == null) return null
        return try {
            LocalDateTime.parse(dateTimeStr, isoFormatter)
        } catch (e: Exception) {
            parseDate(dateTimeStr)?.atStartOfDay()
        }
    }

    fun formatDateForDisplay(dateStr: String?): String {
        val date = parseDate(dateStr) ?: return "..."
        return date.format(displayFormatter)
    }

    fun formatToIso(dateTime: LocalDateTime): String {
        return dateTime.format(isoFormatter)
    }

    fun daysBetween(start: LocalDate, end: LocalDate): Int {
        return ChronoUnit.DAYS.between(start, end).toInt()
    }
}
