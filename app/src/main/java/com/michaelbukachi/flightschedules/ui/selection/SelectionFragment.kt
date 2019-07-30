package com.michaelbukachi.flightschedules.ui.selection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import kotlinx.android.synthetic.main.fragment_selection.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class SelectionFragment : Fragment() {

    private val viewModel: SelectionViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener = object : OnClickListener {
            override fun onClick(schedule: FlightSchedule) {
                Timber.i("Clicked")
            }
        }
        val adapter = FlightSchedulesAdapter(emptyList(), listener)
        origin.setOnItemSelectedListener { _, _, _, item ->
            viewModel.originAirportCode = viewModel.airportCodes[item as String]!!
            viewModel.fetchSchedules()
        }
        destination.setOnItemSelectedListener { _, _, _, item ->
            viewModel.destinationAirportCode = viewModel.airportCodes[item as String]!!
            viewModel.fetchSchedules()
        }

        schedulesList.setHasFixedSize(true)
        schedulesList.adapter = adapter

        viewModel.showMessage.observe(this, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })

        viewModel.isLoading.observe(this, Observer {
            origin.isEnabled = !it
            destination.isEnabled = !it
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.airportsFetched.observe(this, Observer {
            origin.setItems(it)
            destination.setItems(it)
        })

        viewModel.flightSchedule.observe(this, Observer {
            if (it.isEmpty()) {
                blank.visibility = View.VISIBLE
            } else {
                blank.visibility = View.GONE
                adapter.updateData(it)
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.value = true
            viewModel.fetchAirports()
            viewModel.isLoading.value = false
        }
    }


}
