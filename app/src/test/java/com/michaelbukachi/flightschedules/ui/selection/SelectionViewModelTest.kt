package com.michaelbukachi.flightschedules.ui.selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.michaelbukachi.flightschedules.DependenciesRule
import com.michaelbukachi.flightschedules.data.api.Airport
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepo
import com.michaelbukachi.flightschedules.observeOnce
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito.`when`

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SelectionViewModelTest : KoinTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val dependenciesRule = DependenciesRule()

    private val flightSchedulesRepo: FlightSchedulesRepo by inject()
    private val selectionViewModel: SelectionViewModel by inject()

    @Test
    fun `test empty data message is shown when no airports found`() = runBlocking {
        declareMock<FlightSchedulesRepo>()
        `when`(flightSchedulesRepo.getAirports()).thenReturn(emptyList())
        selectionViewModel.fetchAirports()
        selectionViewModel.showMessage.observeOnce {
            assertEquals("No data found.", it)
        }
    }

    @Test
    fun `test fields are update when airports are returned`() = runBlocking {
        val airports = listOf<Airport>(
            Airport("AAA", "Test", 1.2f, 3.4f)
        )
        declareMock<FlightSchedulesRepo>()
        `when`(flightSchedulesRepo.getAirports()).thenReturn(airports)
        selectionViewModel.fetchAirports()
        selectionViewModel.airportsFetched.observeOnce {
            assertEquals(listOf("Test (AAA)"), it)
        }
        assertEquals(selectionViewModel.originAirport, airports[0])
        assertEquals(selectionViewModel.destinationAirport, airports[0])

    }

    @Test
    fun `test duplicate message is shown when selections are the same `() = runBlocking {
        selectionViewModel.fetchSchedules()
        selectionViewModel.showMessage.observeOnce {
            assertEquals("Origin and Destination cannot be the same", it)
        }
    }

    @Test
    fun `test empty data message is shown when no flight schedules are found`() = runBlocking {
        declareMock<FlightSchedulesRepo>()
        `when`(flightSchedulesRepo.getFlightSchedules(any(), any())).thenReturn(emptyList())
        selectionViewModel.originAirport = Airport("AAA", "Test", 1.2f, 3.4f)
        selectionViewModel.destinationAirport = Airport("BBB", "Test", 1.2f, 3.4f)
        selectionViewModel.fetchSchedules()
        selectionViewModel.showMessage.observeOnce {
            assertEquals("No data found.", it)
        }
    }

    @Test
    fun `test fields are updated when flight schedules are found`() = runBlocking {
        val schedules = listOf(
            FlightSchedule("PT1H30M", emptyList(), true)
        )
        declareMock<FlightSchedulesRepo>()
        `when`(flightSchedulesRepo.getFlightSchedules(any(), any())).thenReturn(schedules)
        selectionViewModel.originAirport = Airport("AAA", "Test", 1.2f, 3.4f)
        selectionViewModel.destinationAirport = Airport("BBB", "Test", 1.2f, 3.4f)
        selectionViewModel.fetchSchedules()
        selectionViewModel.flightSchedule.observeOnce {
            assertEquals(schedules, it)
        }
    }
}