package com.michaelbukachi.flightschedules.data.repos

import com.michaelbukachi.flightschedules.BuildConfig
import com.michaelbukachi.flightschedules.data.Auth
import com.michaelbukachi.flightschedules.data.api.Airport
import com.michaelbukachi.flightschedules.data.api.ApiService
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.HttpException
import timber.log.Timber

class FlightSchedulesRepoImpl(apiService: ApiService) : FlightSchedulesRepo {


    private val luftService = apiService.luftService

    override suspend fun refreshToken() {
        try {
            val payload = mutableMapOf(
                "client_id" to BuildConfig.LUFTHANSA_KEY,
                "client_secret" to BuildConfig.LUTFHANSA_SECRET,
                "grant_type" to "client_credentials"
            )
            val response = luftService.getAccessToken(payload)
            var now = LocalDateTime.now()
            now = now.plusSeconds(response.expiresIn.toLong())
            Auth.accessToken = response.accessToken
            Auth.tokenType = response.tokenType
            Auth.expiresAt = now.format(Auth.formatter)
        } catch (e: HttpException) {
            Timber.e("${e.code()} ${e.message()}")
        }
    }

    override suspend fun getAirports(): List<Airport> {
        return try {
            luftService.getAirports().airports
        } catch (e: HttpException) {
            Timber.e("${e.code()} ${e.message()}")
            emptyList()
        }
    }

    override suspend fun getFlightSchedules(origin: String, destination: String): List<FlightSchedule> {

        val tomorrow = LocalDate.now().plusDays(1)
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        return try {
            luftService.getFlightSchedules(origin, destination, formatter.format(tomorrow)).schedule
        } catch (e: HttpException) {
            Timber.e("${e.code()} ${e.message()}")
            emptyList()
        }
    }
}