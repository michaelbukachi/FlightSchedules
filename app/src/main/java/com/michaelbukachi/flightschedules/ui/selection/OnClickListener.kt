package com.michaelbukachi.flightschedules.ui.selection

import com.michaelbukachi.flightschedules.domain.models.FlightSchedule

interface OnClickListener {
    fun onClick(schedule: FlightSchedule)
}