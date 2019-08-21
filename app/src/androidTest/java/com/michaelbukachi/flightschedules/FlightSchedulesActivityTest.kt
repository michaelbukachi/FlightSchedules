package com.michaelbukachi.flightschedules

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.michaelbukachi.flightschedules.ui.FlightSchedulesActivity
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FlightSchedulesActivityTest {

    private val mockWebServer = MockWebServer()

    @Test
    fun testNoFlightSchedulesLoadedWhenSameAirportsAreSelected() {
        mockWebServer.res(200, readFile("airports.json"))
        mockWebServer.res(200, readFile("flight_schedules.json"))
        mockWebServer.start(12346)
        ActivityScenario.launch(FlightSchedulesActivity::class.java)
        SystemClock.sleep(2000)
        onView(withId(R.id.origin)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(0).perform(click())
        onView(withId(R.id.destination)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(0).perform(click())
        SystemClock.sleep(2000)
        onView(withId(R.id.blank)).check(matches(isDisplayed()))
        mockWebServer.shutdown()
    }

    @Test
    fun testFlightSchedulesAreLoadedWhenDifferentAirportsAreSelected() {
        mockWebServer.res(200, readFile("airports.json"))
        mockWebServer.res(200, readFile("flight_schedules.json"))
        mockWebServer.start(12346)
        ActivityScenario.launch(FlightSchedulesActivity::class.java)
        SystemClock.sleep(2000)
        onView(withId(R.id.destination)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(1).perform(click())
        SystemClock.sleep(2000)
        onView(withId(R.id.blank)).check(matches(not(isDisplayed())))
        mockWebServer.shutdown()
    }
}