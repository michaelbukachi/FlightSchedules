package com.michaelbukachi.flightschedules.domain.models


data class FlightSchedule(val duration: String, val points: List<FlightSchedulePoint>, val isDirect: Boolean)