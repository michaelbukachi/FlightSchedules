package com.michaelbukachi.flightschedules.ui.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepo
import com.michaelbukachi.flightschedules.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectionViewModel(private val flightSchedulesRepo: FlightSchedulesRepo) : ViewModel() {

    val showMessage = SingleLiveEvent<String>()
    val isLoading = MutableLiveData<Boolean>()
    val airportsFetched = MutableLiveData<List<String>>()
    val flightSchedule = MutableLiveData<List<FlightSchedule>>()
    val airportCodes = mutableMapOf<String, String>()
    var originAirportCode = ""
    var destinationAirportCode = ""

    suspend fun fetchAirports() = withContext(Dispatchers.IO) {
        val airports = flightSchedulesRepo.getAirports()
        if (airports.isNotEmpty()) {
            originAirportCode = airports[0].code
            destinationAirportCode = airports[0].code
            airportsFetched.postValue(airports.map {
                val string = "${it.name} (${it.code})"
                airportCodes[string] = it.code
                string
            })
        } else {
            showMessage.postValue("No data found.")
        }

    }

    fun fetchSchedules() = viewModelScope.launch {
        if (originAirportCode == destinationAirportCode) {
            showMessage.value = "Origin and Destination cannot be the same"
            return@launch
        }

        isLoading.value = true
        withContext(Dispatchers.IO) {
            val schedules = flightSchedulesRepo.getFlightSchedules(originAirportCode, destinationAirportCode)
            isLoading.postValue(false)
            if (schedules.isNotEmpty()) {
                flightSchedule.postValue(schedules)
            } else {
                flightSchedule.postValue(emptyList())
                showMessage.postValue("No data found.")
            }
        }
    }
}