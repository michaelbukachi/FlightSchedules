package com.michaelbukachi.flightschedules.data.api

import com.michaelbukachi.flightschedules.data.api.response.TokenResponse
import retrofit2.Retrofit
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LuftService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(@FieldMap payload: Map<String, String>): TokenResponse

    suspend fun getAirports()
}


class ApiService(retrofit: Retrofit) {

    val luftService = retrofit.create(LuftService::class.java)
}