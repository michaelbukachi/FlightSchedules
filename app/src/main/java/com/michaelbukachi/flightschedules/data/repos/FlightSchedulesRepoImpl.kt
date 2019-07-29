package com.michaelbukachi.flightschedules.data.repos

import android.content.Context
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.data.api.ApiService
import com.michaelbukachi.flightschedules.data.api.Auth
import com.michaelbukachi.flightschedules.data.api.payload.AuthPayload
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import timber.log.Timber

class FlightSchedulesRepoImpl(apiService: ApiService, private val context: Context) : FlightSchedulesRepo {

    private val luftService = apiService.luftService

    override suspend fun refreshToken() {
        try {
            val payload = AuthPayload(
                context.getString(R.string.lufthansa_key),
                context.getString(R.string.lufthansa_secret), "client_credentials"
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

    override suspend fun getAirports() {
        if (Auth.hasExpired()) {
            refreshToken()
        }


    }
}