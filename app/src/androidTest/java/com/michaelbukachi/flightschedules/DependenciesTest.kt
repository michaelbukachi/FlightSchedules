package com.michaelbukachi.flightschedules

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.michaelbukachi.flightschedules.di.appModules
import com.michaelbukachi.flightschedules.di.dataModules
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

@SmallTest
@RunWith(AndroidJUnit4::class)
class DependenciesTest : KoinTest {

    @Test
    fun checkModules() {
        // use koinApplication instead of startKoin, to avoid having to close Koin after each test
        koinApplication { modules(listOf(testContext, appModules, dataModules)) }.checkModules()
    }
}