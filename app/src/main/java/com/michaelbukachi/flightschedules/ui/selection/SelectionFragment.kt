package com.michaelbukachi.flightschedules.ui.selection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import kotlinx.android.synthetic.main.fragment_selection.*
import org.koin.android.ext.android.inject


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
                findNavController().navigate(
                    SelectionFragmentDirections.actionSelectionFragmentToMapFragment(
                        viewModel.originAirport!!,
                        viewModel.destinationAirport!!
                    )
                )
            }
        }
        val adapter = FlightSchedulesAdapter(emptyList(), listener)
        origin.setOnItemSelectedListener { _, _, _, item ->
            val selection = item as String
            if (viewModel.getOriginAirportCode() == null || !selection.contains(viewModel.getOriginAirportCode()!!)) {
                viewModel.setOriginAirport(selection)
                viewModel.fetchSchedules()
            }

        }
        destination.setOnItemSelectedListener { _, _, _, item ->
            val selection = item as String
            if (viewModel.getDestinationAirportCode() == null || !selection.contains(viewModel.getDestinationAirportCode()!!)) {
                viewModel.setDestinationAirport(selection)
                viewModel.fetchSchedules()
            }
        }

        schedulesList.setHasFixedSize(true)
        schedulesList.adapter = adapter

        viewModel.showMessage.observe(this, Observer {
            Snackbar.make(container, it, Snackbar.LENGTH_SHORT).show()
        })

        viewModel.isLoading.observe(this, Observer {
            origin.isEnabled = !it
            destination.isEnabled = !it
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.airportsFetched.observe(this, Observer {
            origin.setItems(it)
            origin.selectedIndex = 0
            destination.setItems(it)
            destination.selectedIndex = 0
        })

        viewModel.flightSchedule.observe(this, Observer {
            if (it.isEmpty()) {
                blank.visibility = View.VISIBLE
            } else {
                blank.visibility = View.GONE
            }
            adapter.updateData(it)
        })

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.value = true
            viewModel.fetchAirports()
            viewModel.isLoading.value = false
        }
    }


}
