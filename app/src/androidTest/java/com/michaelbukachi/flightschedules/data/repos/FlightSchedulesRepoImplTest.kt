package com.michaelbukachi.flightschedules.data.repos

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.michaelbukachi.flightschedules.MockServerRule
import com.michaelbukachi.flightschedules.data.api.Auth
import com.michaelbukachi.flightschedules.data.api.clearPref
import com.michaelbukachi.flightschedules.res
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@SmallTest
@RunWith(AndroidJUnit4::class)
class FlightSchedulesRepoImplTest : KoinTest {

    @get:Rule
    val mockServerRule = MockServerRule()

    private val repo: FlightSchedulesRepo by inject()

    @Test
    fun testRefreshTokenIsSuccessful() = runBlocking {
        val res = """
            {
              "access_token": "d8bmzggu72dy69tzkffe6vaa",
              "token_type": "bearer",
              "expires_in": 21600
            }
        """.trimIndent()
        mockServerRule.server.res(200, res)
        repo.refreshToken()
        assertTrue(Auth.accessToken.isNotEmpty())
    }

    @After
    fun tearDown() {
        clearPref()
    }
}