package com.michaelbukachi.flightschedules.ui.selection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.di.injector
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import kotlinx.android.synthetic.main.fragment_selection.*
import kotlinx.coroutines.launch
import timber.log.Timber


class SelectionFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(activity!!, activity!!.injector.selectionViewModelFactory())
            .get(SelectionViewModel::class.java)
    }

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
                if (schedule.isDirect) {
                    Timber.i("Is a direct flight")
                    findNavController().navigate(
                        SelectionFragmentDirections.actionSelectionFragmentToMapFragment(
                            Route(
                                listOf(
                                    viewModel.originAirport!!,
                                    viewModel.destinationAirport!!
                                )
                            )
                        )
                    )
                } else {
                    Timber.i("Not a direct flight")
                    lifecycleScope.launch {
                        val airports = mutableListOf<Airport>()
                        airports.add(viewModel.originAirport!!)
                        progressBar.visibility = View.VISIBLE
                        airports.addAll(viewModel.getAirportsFromSchedule(schedule.points))
                        progressBar.visibility = View.GONE
                        airports.add(viewModel.destinationAirport!!)
                        findNavController().navigate(
                            SelectionFragmentDirections.actionSelectionFragmentToMapFragment(
                                Route(airports)
                            )
                        )
                    }
                }
            }
        }

        val adapter = FlightSchedulesAdapter(emptyList(), listener)
        origin.setOnItemSelectedListener { _, pos, _, item ->
            val selection = item as String
            viewModel.originIndex = pos
            if (viewModel.getOriginAirportCode() == null || !selection.contains(viewModel.getOriginAirportCode()!!)) {
                viewModel.setOriginAirport(selection)
                viewModel.fetchSchedules()
            }

        }
        destination.setOnItemSelectedListener { _, pos, _, item ->
            val selection = item as String
            viewModel.destinationIndex = pos
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
            origin.selectedIndex = viewModel.originIndex
            destination.setItems(it)
            destination.selectedIndex = viewModel.destinationIndex
        })

        viewModel.flightSchedule.observe(this, Observer {
            blank.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            adapter.updateData(it)
        })

        lifecycleScope.launchWhenCreated {
            if (!viewModel.airportsFetched()) {
                viewModel.isLoading.value = true
                viewModel.fetchAirports()
                viewModel.isLoading.value = false
            }

        }
    }


}
