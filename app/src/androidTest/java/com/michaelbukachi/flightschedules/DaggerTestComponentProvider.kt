package com.michaelbukachi.flightschedules

import com.michaelbukachi.flightschedules.di.ApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerComponentProvider

interface DaggerTestComponentProvider: DaggerComponentProvider {
    override val component: TestApplicationComponent
}