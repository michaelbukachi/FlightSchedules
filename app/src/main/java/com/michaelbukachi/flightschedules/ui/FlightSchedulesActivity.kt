package com.michaelbukachi.flightschedules.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.ui.selection.SelectionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FlightSchedulesActivity : AppCompatActivity() {

    // Bind viewmodel lifecycle to activity instead of fragment
    private val viewModel: SelectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_schedules)
    }

    override fun onNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
}
