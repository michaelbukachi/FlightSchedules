package com.michaelbukachi.flightschedules.data

import com.chibatching.kotpref.KotprefModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

const val DATE_FORMAT = "dd MM yyyy HH:mm:ss"

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
        Timber.i("Expiry is $expiresAt")
        val expiry = LocalDateTime.parse(expiresAt, formatter)
        return now > expiry
    }
}

// Clear all preferences
fun clearPref() {
    Auth.clear()
}