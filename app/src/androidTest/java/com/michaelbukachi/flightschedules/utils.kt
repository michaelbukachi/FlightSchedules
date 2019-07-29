package com.michaelbukachi.flightschedules

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.dsl.module

val testContext = module {
    single { InstrumentationRegistry.getInstrumentation().context }
}

fun MockWebServer.res(code: Int, body: String) {
    enqueue(MockResponse().apply {
        setResponseCode(code)
        setBody(body)
    })
}

fun MockWebServer.timeout() {
    enqueue(MockResponse().apply {
        socketPolicy = SocketPolicy.NO_RESPONSE
    })
}


class MockServerRule : TestRule {
    lateinit var server: MockWebServer
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                server = MockWebServer()
                server.url("/")
//                server.start()

//                startKoin { modules(listOf(testContext, dataModules, mockModule)) }
                base?.evaluate()
//                stopKoin()
                server.shutdown()
            }
        }
    }
}