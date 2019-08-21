package com.michaelbukachi.flightschedules

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy

fun MockWebServer.res(code: Int, body: String) {
    enqueue(MockResponse().setResponseCode(code).setBody(body))
}

fun MockWebServer.timeout() {
    enqueue(MockResponse().apply {
        socketPolicy = SocketPolicy.NO_RESPONSE
    })
}

fun readFile(fileName: String): String {
    return FlightSchedulesActivityTest::class.java.getResource(fileName)?.readText() ?: ""
}