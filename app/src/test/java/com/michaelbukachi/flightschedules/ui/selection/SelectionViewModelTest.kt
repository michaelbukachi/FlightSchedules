package com.michaelbukachi.flightschedules.ui.selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.michaelbukachi.flightschedules.CoroutinesRule
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.usecases.FlightsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SelectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @MockK
    lateinit var flightsUseCase: FlightsUseCase

    @InjectMockKs
    lateinit var selectionViewModel: SelectionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test empty data message is shown when no airports found`() = runBlockingTest {
        coEvery { flightsUseCase.getAirports() } returns emptyList()
        selectionViewModel.fetchAirports()
        selectionViewModel.showMessage.test().assertValue("No data found.")
    }

    @Test
    fun `test fields are update when airports are returned`() = runBlockingTest {
        val airport = Airport("AAA", "Test", 1.2f, 3.4f)
        coEvery { flightsUseCase.getAirports() } returns listOf("Test (AAA)")
        every { flightsUseCase.getDefaultAirport() } returns airport
        selectionViewModel.fetchAirports()
        selectionViewModel.airportsFetched.test().assertValue(listOf("Test (AAA)"))
        assertThat(selectionViewModel.originAirport, `is`(airport))
        assertThat(selectionViewModel.destinationAirport, `is`(airport))
    }

    @Test
    fun `test duplicate message is shown when selections are the same `() = runBlockingTest {
        selectionViewModel.fetchSchedules()
        selectionViewModel.showMessage.test()
            .assertValue("Origin and Destination cannot be the same")
    }

    @Test
    fun `test empty data message is shown when no flight schedules are found`() = runBlockingTest {
        coEvery { flightsUseCase.getFlightSchedules(any(), any()) } returns emptyList()
        selectionViewModel.originAirport = Airport("AAA", "Test", 1.2f, 3.4f)
        selectionViewModel.destinationAirport = Airport("BBB", "Test", 1.2f, 3.4f)
        selectionViewModel.fetchSchedules()
        selectionViewModel.showMessage.test().assertValue("No data found.")
    }

    @Test
    fun `test fields are updated when flight schedules are found`() = runBlockingTest {
        val schedules = listOf(
            FlightSchedule("PT1H30M", emptyList(), true)
        )
        coEvery { flightsUseCase.getFlightSchedules(any(), any()) } returns schedules
        selectionViewModel.originAirport = Airport("AAA", "Test", 1.2f, 3.4f)
        selectionViewModel.destinationAirport = Airport("BBB", "Test", 1.2f, 3.4f)
        selectionViewModel.fetchSchedules()
        selectionViewModel.flightSchedule.test().assertValue(schedules)
    }
}