package com.michaelbukachi.flightschedules.data.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @Expose @SerializedName("access_token") val accessToken: String,
    @Expose @SerializedName("token_type") val tokenType: String,
    @Expose @SerializedName("expires_in") val expiresIn: Int
)