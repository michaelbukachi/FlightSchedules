package com.michaelbukachi.flightschedules.domain.models

data class FlightSchedulePoint(
    val departureAirport: String, val departureTime: String, val arrivalAirport: String,
    val arrivalTime: String, val airlineId: String, val flightNo: Int
)