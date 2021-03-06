package com.michaelbukachi.flightschedules

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestInstrumentationRunner: AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestFlightSchedules::class.java.name, context)
    }
}