package com.michaelbukachi.flightschedules.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.michaelbukachi.flightschedules.R

class FlightSchedulesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_schedules)
    }

    override fun onNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
}
