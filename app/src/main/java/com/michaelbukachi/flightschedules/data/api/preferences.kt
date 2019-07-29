package com.michaelbukachi.flightschedules.data.api

import com.chibatching.kotpref.KotprefModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

const val DATE_FORMAT = "dd MM YYYY HH:mm:ss"

object Auth : KotprefModel() {
    var accessToken by stringPref("")
    var tokenType by stringPref("")
    var expiresAt by stringPref("")

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

    fun hasExpired(): Boolean {
        if (accessToken.isEmpty()) {
            return true
        }
        val now = LocalDateTime.now()
        val expiry = LocalDateTime.from(formatter.parse(expiresAt))
        return now > expiry
    }
}

// Clear all preferences
fun clearPref() {
    Auth.clear()
}