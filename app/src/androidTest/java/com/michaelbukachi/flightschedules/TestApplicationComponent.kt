package com.michaelbukachi.flightschedules

import android.content.Context
import com.michaelbukachi.flightschedules.di.ApplicationComponent
import com.michaelbukachi.flightschedules.di.DataModule
import com.michaelbukachi.flightschedules.di.DomainModule
import dagger.BindsInstance
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(modules = [MockUrlModule::class, DataModule::class, DomainModule::class])
interface TestApplicationComponent: ApplicationComponent {

    fun getMockWebServer(): MockWebServer

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): TestApplicationComponent
    }
}