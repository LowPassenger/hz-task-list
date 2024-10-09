package com.herc.test.hztasklist.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object DateTimeUtil {
    const val DATE_TIME_PATTERN: String = "dd/MM/yyyy HH:mm"

    fun toMillis(localDateTime: LocalDateTime): Long {
        val formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
        val formattedDateTime = localDateTime.format(formatter)
        val dateTime = LocalDateTime.parse(formattedDateTime, formatter)
        return Date.from(dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()).time
    }

    fun toDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())
    }
}