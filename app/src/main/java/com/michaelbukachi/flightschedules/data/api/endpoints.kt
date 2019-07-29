package com.michaelbukachi.flightschedules.data.api

import com.michaelbukachi.flightschedules.data.api.response.TokenResponse
import retrofit2.Retrofit
import retrofit2.http.*

interface LuftService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(@FieldMap payload: Map<String, String>): TokenResponse

    @Headers("Accept: application/json")
    @GET("mds-references/airports/?lang=EN")
    suspend fun getAirports(): AirportResponse

    @Headers("Accept: application/json")
    @GET("operations/schedules/{origin}/{destination}/{fromDateTime}")
    suspend fun getFlightSchedules(
        @Path("origin") origin: String,
        @Path("destination") destination: String,
        @Path("fromDateTime") fromDateTime: String
    ): ScheduleResponse
}


class ApiService(retrofit: Retrofit) {

    val luftService = retrofit.create(LuftService::class.java)
}