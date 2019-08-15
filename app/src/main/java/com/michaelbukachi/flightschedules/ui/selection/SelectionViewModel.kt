package com.michaelbukachi.flightschedules.ui.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.usecases.FlightsUseCase
import com.michaelbukachi.flightschedules.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SelectionViewModel(private val flightsUseCase: FlightsUseCase) : ViewModel() {

    val showMessage = SingleLiveEvent<String>()
    val reset = SingleLiveEvent<Void>()
    val isLoading = MutableLiveData<Boolean>()
    val airportsFetched = MutableLiveData<List<String>>()
    val flightSchedule = MutableLiveData<List<FlightSchedule>>()
    var originAirport: Airport? = null
    var destinationAirport: Airport? = null
    var originIndex = 0
    var destinationIndex = 0


    suspend fun fetchAirports() = withContext(Dispatchers.IO) {
        val airports = flightsUseCase.getAirports()
        if (airports.isNotEmpty()) {
            airportsFetched.postValue(airports)
            originAirport = flightsUseCase.getDefaultAirport()
            destinationAirport = flightsUseCase.getDefaultAirport()
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
            val schedules = flightsUseCase.getFlightSchedules(originAirport!!.code, destinationAirport!!.code)
            isLoading.postValue(false)
            if (schedules.isNotEmpty()) {
                flightSchedule.postValue(schedules)
            } else {
                flightSchedule.postValue(emptyList())
                showMessage.postValue("No data found.")
            }
        }
    }

    suspend fun getAirport(code: String): Airport? = withContext(Dispatchers.IO) {
        Timber.i("Getting airport details for $code")
        return@withContext flightsUseCase.getAirportDetails(code)
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