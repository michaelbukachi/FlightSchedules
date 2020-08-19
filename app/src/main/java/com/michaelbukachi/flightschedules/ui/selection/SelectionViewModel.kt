package com.michaelbukachi.flightschedules.ui.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.models.FlightSchedulePoint
import com.michaelbukachi.flightschedules.domain.usecases.FlightsUseCase
import com.michaelbukachi.flightschedules.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SelectionViewModel @Inject constructor(private val flightsUseCase: FlightsUseCase) : ViewModel() {

    val showMessage = SingleLiveEvent<String>()
    val isLoading = MutableLiveData<Boolean>()
    val airportsFetched = MutableLiveData<List<String>>()
    val flightSchedule = MutableLiveData<List<FlightSchedule>>()
    var originAirport: Airport? = null
    var destinationAirport: Airport? = null
    var originIndex = 0
    var destinationIndex = 0


    suspend fun fetchAirports() {
        val airports = flightsUseCase.getAirports()
        if (airports.isNotEmpty()) {
            airportsFetched.value = airports
            originAirport = flightsUseCase.getDefaultAirport()
            destinationAirport = flightsUseCase.getDefaultAirport()
        } else {
            showMessage.value = "No data found."
        }
        flightSchedule.value = emptyList()
    }

    fun fetchSchedules() = viewModelScope.launch {
        if (originAirport?.code == destinationAirport?.code) {
            showMessage.value = "Origin and Destination cannot be the same"
            flightSchedule.value = emptyList()
            return@launch
        }

        isLoading.value = true
        val schedules =
            flightsUseCase.getFlightSchedules(originAirport!!.code, destinationAirport!!.code)
        isLoading.value = false
        if (schedules.isNotEmpty()) {
            flightSchedule.value = schedules
        } else {
            flightSchedule.value = emptyList()
            showMessage.value = "No data found."
        }
    }

    suspend fun getAirport(code: String): Airport? {
        Timber.i("Getting airport details for $code")
        return flightsUseCase.getAirportDetails(code)
    }

    suspend fun getAirportsFromSchedule(points: List<FlightSchedulePoint>): List<Airport> {
        val airports = mutableListOf<Airport>()
        for (i in 1 until points.size) {
            val fs = points[i]
            val airport = getAirport(fs.departureAirport)
            airport?.let {
                airports.add(it)
            }
        }
        return airports
    }

    fun getOriginAirportCode(): String? = originAirport?.code

    fun getDestinationAirportCode(): String? = originAirport?.code

    fun setOriginAirport(name: String) {
        originAirport = flightsUseCase.getAirportByName(name)!!
    }

    fun setDestinationAirport(name: String) {
        destinationAirport = flightsUseCase.getAirportByName(name)!!
    }

    fun airportsFetched() = flightsUseCase.airportsFetched()
}