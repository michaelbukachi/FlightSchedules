package com.michaelbukachi.flightschedules

import android.os.SystemClock
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.michaelbukachi.flightschedules.ui.selection.SelectionFragment
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelectionFragmentTest {

    private val mockWebServer = MockWebServer()

    @Test
    fun testNoFlightSchedulesLoadedWhenSameAirportsAreSelected() {
        mockWebServer.res(200, readFile("airports.json"))
        mockWebServer.res(200, readFile("flight_schedules.json"))
        mockWebServer.start(12346)
        launchFragmentInContainer<SelectionFragment>(null, R.style.AppTheme, null)
        SystemClock.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.origin)).perform(ViewActions.click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java))))
            .atPosition(0).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.destination)).perform(ViewActions.click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java))))
            .atPosition(0).perform(ViewActions.click())
        SystemClock.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.blank))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        mockWebServer.shutdown()
    }

    @Test
    fun testFlightSchedulesAreLoadedWhenDifferentAirportsAreSelected() {
        mockWebServer.res(200, readFile("airports.json"))
        mockWebServer.res(200, readFile("flight_schedules.json"))
        mockWebServer.start(12346)
        launchFragmentInContainer<SelectionFragment>(null, R.style.AppTheme, null)
        SystemClock.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.destination)).perform(ViewActions.click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java))))
            .atPosition(1).perform(ViewActions.click())
        SystemClock.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.blank))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        mockWebServer.shutdown()
    }
}