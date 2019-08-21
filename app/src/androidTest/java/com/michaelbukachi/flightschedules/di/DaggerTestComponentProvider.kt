package com.michaelbukachi.flightschedules.di

interface DaggerTestComponentProvider: DaggerComponentProvider {
    override val component: TestApplicationComponent
}