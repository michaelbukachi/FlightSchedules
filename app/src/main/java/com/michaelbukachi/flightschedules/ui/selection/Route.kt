package com.michaelbukachi.flightschedules.ui.selection

import android.os.Parcelable
import com.michaelbukachi.flightschedules.domain.models.Airport
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(val airports: List<Airport>) : Parcelable