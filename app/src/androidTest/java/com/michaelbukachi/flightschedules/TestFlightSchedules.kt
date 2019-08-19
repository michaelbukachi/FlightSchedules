package com.michaelbukachi.flightschedules

class TestFlightSchedules: FlightSchedules(), DaggerTestComponentProvider {

    override val component: TestApplicationComponent
        get() = DaggerTestApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
}