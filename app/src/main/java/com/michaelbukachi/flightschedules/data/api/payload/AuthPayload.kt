package com.michaelbukachi.flightschedules.data.api.payload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthPayload(
    @Expose @SerializedName("client_id") val clientId: String,
    @Expose @SerializedName("client_secret") val clientSecret: String,
    @Expose @SerializedName("grant_type") val grantType: String
)