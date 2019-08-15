package com.michaelbukachi.flightschedules.domain.usecases

import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.repos.FlightSchedulesRepo

class FlightsUseCase(val flightSchedulesRepo: FlightSchedulesRepo) {
    private var airportsByCode = mutableMapOf<String, Airport>()
    private var fetchedAirports = emptyList<Airport>()
    suspend fun getAirports(): List<String> {
        fetchedAirports = flightSchedulesRepo.getAirports()

        return fetchedAirports.map {
            airportsByCode[it.code] = it
            "${it.name} (${it.code})"
        }
    }

    suspend fun getAirportDetails(code: String): Airport? = flightSchedulesRepo.getAirport(code)

    fun getAirportByName(name: String): Airport? {
        val result = ".*\\((.*)\\)".toRegex().find(name)
        if (result != null) {
            val code = result.groupValues[1]
            return airportsByCode[code]
        }
        return null
    }

    fun airportsFetched(): Boolean = fetchedAirports.isNotEmpty()

    fun getDefaultAirport(): Airport? = if (airportsFetched()) fetchedAirports[0] else null

    suspend fun getFlightSchedules(origin: String, destination: String): List<FlightSchedule> =
        flightSchedulesRepo.getFlightSchedules(origin, destination)
}