package com.michaelbukachi.flightschedules.domain.repos

import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule

interface FlightSchedulesRepo {

    suspend fun refreshToken()

    suspend fun getAirports(): List<Airport>

    suspend fun getAirport(code: String): Airport?

    suspend fun getFlightSchedules(origin: String, destination: String): List<FlightSchedule>
}