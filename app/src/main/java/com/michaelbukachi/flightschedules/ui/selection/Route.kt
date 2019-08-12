package com.michaelbukachi.flightschedules.ui.selection

import android.os.Parcelable
import com.michaelbukachi.flightschedules.data.api.Airport
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(val airports: List<Airport>) : Parcelable