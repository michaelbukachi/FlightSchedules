package com.michaelbukachi.flightschedules.data.api

import com.michaelbukachi.flightschedules.data.api.payload.AuthPayload
import com.michaelbukachi.flightschedules.data.api.response.TokenResponse
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LuftService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("oauth/token")
    suspend fun getAccessToken(@Body payload: AuthPayload): TokenResponse

    suspend fun getAirports()
}


class ApiService(retrofit: Retrofit) {

    val luftService = retrofit.create(LuftService::class.java)
}