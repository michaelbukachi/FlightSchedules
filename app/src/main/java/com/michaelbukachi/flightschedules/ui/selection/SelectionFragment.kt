package com.michaelbukachi.flightschedules.ui.selection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.tiper.MaterialSpinner
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
        origin.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selection = parent.selectedItem as String
                viewModel.originIndex = position
                viewModel.setOriginAirport(selection)
                viewModel.fetchSchedules()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        destination.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selection = parent.selectedItem as String
                viewModel.destinationIndex = position
                viewModel.setDestinationAirport(selection)
                viewModel.fetchSchedules()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            val originAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            val destinationAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            origin.adapter = originAdapter
            destination.adapter = destinationAdapter
            origin.selection = viewModel.originIndex
            destination.selection = viewModel.destinationIndex
        })

        viewModel.flightSchedule.observe(this, Observer {
            blank.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            adapter.updateData(it)
        })
        lifecycleScope.launch {
            if (!viewModel.airportsFetched()) {
                viewModel.isLoading.value = true
                viewModel.fetchAirports()
                viewModel.isLoading.value = false
            }

        }
    }


}
