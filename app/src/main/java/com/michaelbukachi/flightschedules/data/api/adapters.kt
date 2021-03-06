package com.michaelbukachi.flightschedules.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.models.FlightSchedulePoint

data class AirportResponse(val airports: List<Airport>)


val airportsDeserializer = JsonDeserializer { json, _, _ ->
    val finalList = mutableListOf<Airport>()
    val jsonObject = json.asJsonObject
    if (jsonObject.getAsJsonObject("AirportResource")
            .getAsJsonObject("Airports").get("Airport").isJsonArray
    ) {
        val airportsList = jsonObject.getAsJsonObject("AirportResource")
            .getAsJsonObject("Airports")
            .getAsJsonArray("Airport")

        airportsList.forEach {
            val airportObject = it.asJsonObject
            val code = airportObject.get("AirportCode").asString
            val coordinateObject = airportObject.getAsJsonObject("Position")
                .getAsJsonObject("Coordinate")
            val latitude = coordinateObject.get("Latitude").asFloat
            val longitude = coordinateObject.get("Longitude").asFloat
            val nameObject = airportObject.getAsJsonObject("Names")
                .getAsJsonObject("Name")
            val name = nameObject.get("$").asString
            finalList.add(Airport(code, name, latitude, longitude))
        }
    } else {
        val airportObject = jsonObject.getAsJsonObject("AirportResource")
            .getAsJsonObject("Airports")
            .getAsJsonObject("Airport")
        val code = airportObject.get("AirportCode").asString
        val coordinateObject = airportObject.getAsJsonObject("Position")
            .getAsJsonObject("Coordinate")
        val latitude = coordinateObject.get("Latitude").asFloat
        val longitude = coordinateObject.get("Longitude").asFloat
        val nameObject = airportObject.getAsJsonObject("Names")
            .getAsJsonObject("Name")
        val name = nameObject.get("$").asString
        finalList.add(Airport(code, name, latitude, longitude))
    }


    return@JsonDeserializer AirportResponse(finalList)
}

data class ScheduleResponse(val schedule: List<FlightSchedule>)


fun getFlightSchedulePoint(jsonObject: JsonObject): FlightSchedulePoint {
    val departureAirport = jsonObject.getAsJsonObject("Departure").get("AirportCode").asString
    val departureTime =
        jsonObject.getAsJsonObject("Departure").getAsJsonObject("ScheduledTimeLocal").get("DateTime").asString
    val arrivalAirport = jsonObject.getAsJsonObject("Arrival").get("AirportCode").asString
    val arrivalTime =
        jsonObject.getAsJsonObject("Arrival").getAsJsonObject("ScheduledTimeLocal").get("DateTime").asString
//    val terminal = jsonObject.getAsJsonObject("Arrival").getAsJsonObject("Terminal").get("Name").asInt
    val airlineId = jsonObject.getAsJsonObject("MarketingCarrier").get("AirlineID").asString
    val flightNo = jsonObject.getAsJsonObject("MarketingCarrier").get("FlightNumber").asInt
    return FlightSchedulePoint(departureAirport, departureTime, arrivalAirport, arrivalTime, airlineId, flightNo)
}

val scheduleDeserializer = JsonDeserializer { json, _, _ ->
    val finalList = mutableListOf<FlightSchedule>()
    val jsonObject = json.asJsonObject
    var scheduleList = JsonArray()
    if (jsonObject.getAsJsonObject("ScheduleResource").get("Schedule").isJsonArray) {
        scheduleList = jsonObject.getAsJsonObject("ScheduleResource")
            .getAsJsonArray("Schedule")
    } else {
        scheduleList.add(jsonObject.getAsJsonObject("ScheduleResource").get("Schedule"))
    }

    scheduleList.forEach {
        val scheduleObject = it.asJsonObject
        val duration = scheduleObject.getAsJsonObject("TotalJourney").get("Duration").asString
        val flightSchedulePoints = mutableListOf<FlightSchedulePoint>()
        if (scheduleObject.get("Flight").isJsonArray) {
            val flightList = scheduleObject.getAsJsonArray("Flight")
            flightList.forEach {
                val flightObject = it.asJsonObject
                flightSchedulePoints.add(getFlightSchedulePoint(flightObject))
            }
        } else {
            val flightObject = scheduleObject.getAsJsonObject("Flight")
            flightSchedulePoints.add(getFlightSchedulePoint(flightObject))
        }
        finalList.add(FlightSchedule(duration, flightSchedulePoints, flightSchedulePoints.size == 1))
    }
    return@JsonDeserializer ScheduleResponse(finalList)
}
