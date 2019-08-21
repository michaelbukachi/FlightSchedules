package com.michaelbukachi.flightschedules

import com.michaelbukachi.flightschedules.di.DaggerTestApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerTestComponentProvider
import com.michaelbukachi.flightschedules.di.TestApplicationComponent

class TestFlightSchedules: FlightSchedules(), DaggerTestComponentProvider {

    override val component: TestApplicationComponent
        get() = DaggerTestApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
}