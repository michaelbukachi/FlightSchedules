package com.michaelbukachi.flightschedules.di

import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepoImpl
import com.michaelbukachi.flightschedules.domain.repos.FlightSchedulesRepo
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

    @Binds
    abstract fun flightScheduleRepo(flightSchedulesRepoImpl: FlightSchedulesRepoImpl): FlightSchedulesRepo
}