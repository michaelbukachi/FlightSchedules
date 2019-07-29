package com.michaelbukachi.flightschedules.data.repos

interface FlightSchedulesRepo {

    suspend fun refreshToken()

    suspend fun getAirports()
}