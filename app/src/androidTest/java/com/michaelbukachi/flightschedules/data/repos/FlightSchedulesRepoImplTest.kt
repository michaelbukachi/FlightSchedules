package com.michaelbukachi.flightschedules.data.repos

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.michaelbukachi.flightschedules.data.api.Auth
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@SmallTest
@RunWith(AndroidJUnit4::class)
class FlightSchedulesRepoImplTest : KoinTest {

//    @get:Rule
//    val mockServerRule = MockServerRule()

    private val repo: FlightSchedulesRepo by inject()

    @Test
    fun testRefreshTokenIsSuccessful() = runBlocking {
        //        val fileName = "auth_ok.json"
//        mockServerRule.server.res(200, fileName)
        repo.refreshToken()
        assertTrue(Auth.accessToken.isNotEmpty())
    }
}