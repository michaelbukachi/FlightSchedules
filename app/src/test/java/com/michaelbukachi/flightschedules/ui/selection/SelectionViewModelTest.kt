package com.michaelbukachi.flightschedules.ui.selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.michaelbukachi.flightschedules.domain.models.Airport
import com.michaelbukachi.flightschedules.domain.models.FlightSchedule
import com.michaelbukachi.flightschedules.domain.usecases.FlightsUseCase
import com.michaelbukachi.flightschedules.observeOnce
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class SelectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var flightsUseCase: FlightsUseCase
    @InjectMocks
    lateinit var selectionViewModel: SelectionViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test empty data message is shown when no airports found`() = runBlocking {
        `when`(flightsUseCase.getAirports()).thenReturn(emptyList())
        selectionViewModel.fetchAirports()
        selectionViewModel.showMessage.observeOnce {
            assertEquals("No data found.", it)
        }
    }

    @Test
    fun `test fields are update when airports are returned`() = runBlocking {
        val airport = Airport("AAA", "Test", 1.2f, 3.4f)
        `when`(flightsUseCase.getAirports()).thenReturn(listOf("Test (AAA)"))
        `when`(flightsUseCase.getDefaultAirport()).thenReturn(airport)
        selectionViewModel.fetchAirports()
        selectionViewModel.airportsFetched.observeOnce {
            assertEquals(listOf("Test (AAA)"), it)
        }
        assertEquals(selectionViewModel.originAirport, airport)
        assertEquals(selectionViewModel.destinationAirport, airport)

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
        `when`(flightsUseCase.getFlightSchedules(any(), any())).thenReturn(emptyList())
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
        `when`(flightsUseCase.getFlightSchedules(any(), any())).thenReturn(schedules)
        selectionViewModel.originAirport = Airport("AAA", "Test", 1.2f, 3.4f)
        selectionViewModel.destinationAirport = Airport("BBB", "Test", 1.2f, 3.4f)
        selectionViewModel.fetchSchedules()
        selectionViewModel.flightSchedule.observeOnce {
            assertEquals(schedules, it)
        }
    }
}