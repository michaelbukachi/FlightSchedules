package com.michaelbukachi.flightschedules.ui.selection

import com.michaelbukachi.flightschedules.data.api.FlightSchedule

interface OnClickListener {
    fun onClick(schedule: FlightSchedule)
}