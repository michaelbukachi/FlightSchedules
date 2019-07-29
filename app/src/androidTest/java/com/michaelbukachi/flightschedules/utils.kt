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

object RestServiceTestHelper {
    fun getStringFromFile(filepath: String): String {
        return this::class.java.classLoader?.getResource(filepath)?.readText() ?: ""
    }
}

fun MockWebServer.res(code: Int, filepath: String) {
    enqueue(MockResponse().apply {
        setResponseCode(code)
        setBody(RestServiceTestHelper.getStringFromFile(filepath))
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
                server.start()
                val mockModule = module {
                    single(override = true) { server.url("/") }
                }
//                startKoin { modules(listOf(testContext, dataModules, mockModule)) }
                base?.evaluate()
//                stopKoin()
                server.shutdown()
            }
        }
    }
}