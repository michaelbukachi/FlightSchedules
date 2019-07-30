package com.michaelbukachi.flightschedules.ui.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelbukachi.flightschedules.data.api.Airport
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepo
import com.michaelbukachi.flightschedules.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectionViewModel(private val flightSchedulesRepo: FlightSchedulesRepo) : ViewModel() {

    val showMessage = SingleLiveEvent<String>()
    val reset = SingleLiveEvent<Void>()
    val isLoading = MutableLiveData<Boolean>()
    val airportsFetched = MutableLiveData<List<String>>()
    val flightSchedule = MutableLiveData<List<FlightSchedule>>()
    private var airports = emptyList<Airport>()
    private var airportsByCode = mutableMapOf<String, Airport>()
    private val airportNames = mutableMapOf<String, String>()
    var originAirport: Airport? = null
    var destinationAirport: Airport? = null


    suspend fun fetchAirports() = withContext(Dispatchers.IO) {
        airports = flightSchedulesRepo.getAirports()
        if (airports.isNotEmpty()) {
            originAirport = airports[0]
            destinationAirport = airports[0]
            airportsFetched.postValue(airports.map {
                val string = "${it.name} (${it.code})"
                airportsByCode[it.code] = it
                airportNames[string] = it.code
                string
            })
        } else {
            showMessage.postValue("No data found.")
        }
        flightSchedule.postValue(emptyList())

    }

    fun fetchSchedules() = viewModelScope.launch {
        if (originAirport?.code == destinationAirport?.code) {
            showMessage.value = "Origin and Destination cannot be the same"
            return@launch
        }

        isLoading.value = true
        withContext(Dispatchers.IO) {
            val schedules = flightSchedulesRepo.getFlightSchedules(originAirport!!.code, destinationAirport!!.code)
            isLoading.postValue(false)
            if (schedules.isNotEmpty()) {
                flightSchedule.postValue(schedules)
            } else {
                flightSchedule.postValue(emptyList())
                showMessage.postValue("No data found.")
            }
        }
    }

    fun getOriginAirportCode(): String? = originAirport?.code

    fun getDestinationAirportCode(): String? = originAirport?.code

    fun setOriginAirport(code: String) {
        originAirport = airportsByCode[airportNames[code]]!!
    }

    fun setDestinationAirport(code: String) {
        destinationAirport = airportsByCode[airportNames[code]]!!
    }
}