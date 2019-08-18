package com.michaelbukachi.flightschedules

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.michaelbukachi.flightschedules.di.ApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerApplicationComponent
import com.michaelbukachi.flightschedules.di.DaggerComponentProvider
import com.michaelbukachi.flightschedules.utils.CrashReportingTree
import timber.log.Timber

class FlightSchedules : Application(), DaggerComponentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
    override fun onCreate() {
        super.onCreate()

//        startKoin {
//            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
//            androidContext(this@FlightSchedules)
//            modules(listOf(appModules, domainModules, dataModules))
//        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        AndroidThreeTen.init(this)
    }
}