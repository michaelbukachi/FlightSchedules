package com.michaelbukachi.flightschedules.di

import android.app.Activity

interface DaggerComponentProvider {
    val component: ApplicationComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component
