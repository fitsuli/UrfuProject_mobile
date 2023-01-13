package ru.fitsuli.petsmobile.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Dmitry Danilyuk at 13.01.2023
 */
object Utils {
    fun formatDateDefault(
        utcDate: String,
        pattern: String = "dd.MM.yyyy HH:mm"
    ): String = ZonedDateTime.ofInstant(
        Instant.parse(utcDate),
        ZoneId.systemDefault()
    ).format(DateTimeFormatter.ofPattern(pattern))
}