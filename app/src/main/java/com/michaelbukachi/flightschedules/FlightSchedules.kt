package com.michaelbukachi.flightschedules

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.michaelbukachi.flightschedules.di.ApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerComponentProvider
import com.michaelbukachi.flightschedules.utils.CrashReportingTree
import timber.log.Timber

open class FlightSchedules : Application(), DaggerComponentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        AndroidThreeTen.init(this)
    }
}