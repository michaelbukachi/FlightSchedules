package com.michaelbukachi.flightschedules

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.michaelbukachi.flightschedules.ui.FlightSchedulesActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlightSchedulesActivityTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestFlightSchedules
//        val appInjector = DaggerTestApplicationComponent.builder()
//            .applicationContext(app)
//            .build()
        mockWebServer = app.component.getMockWebServer()
    }

    @Test
    fun testSpinnersArePopulated() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(readFile("airports.json")))

        ActivityScenario.launch(FlightSchedulesActivity::class.java)
    }
}