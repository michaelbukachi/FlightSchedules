package com.michaelbukachi.flightschedules.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airport(val code: String, val name: String, val latitude: Float, val longitude: Float) : Parcelable