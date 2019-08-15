package com.michaelbukachi.flightschedules

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.michaelbukachi.flightschedules.di.appModules
import com.michaelbukachi.flightschedules.di.dataModules
import com.michaelbukachi.flightschedules.di.domainModules
import com.michaelbukachi.flightschedules.utils.CrashReportingTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import timber.log.Timber

class FlightSchedules : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(this@FlightSchedules)
            modules(listOf(appModules, domainModules, dataModules))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        AndroidThreeTen.init(this)
    }
}