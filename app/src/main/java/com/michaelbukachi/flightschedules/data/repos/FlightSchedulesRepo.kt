package com.michaelbukachi.flightschedules.data.repos

import com.michaelbukachi.flightschedules.data.api.Airport
import com.michaelbukachi.flightschedules.data.api.FlightSchedule

interface FlightSchedulesRepo {

    suspend fun refreshToken()

    suspend fun getAirports(): List<Airport>

    suspend fun getAirport(code: String): Airport?

    suspend fun getFlightSchedules(origin: String, destination: String): List<FlightSchedule>
}