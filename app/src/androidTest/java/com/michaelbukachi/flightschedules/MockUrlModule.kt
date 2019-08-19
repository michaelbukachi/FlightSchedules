package com.michaelbukachi.flightschedules

import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.mockwebserver.MockWebServer
import java.lang.NullPointerException
import javax.inject.Singleton

@Module
object MockUrlModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideMockServer(): MockWebServer {
        var mockWebServer: MockWebServer? = null
        val thread = Thread(Runnable {
            mockWebServer = MockWebServer()
            mockWebServer?.start()
        })
        thread.start()
        thread.join()
        return mockWebServer ?: throw NullPointerException()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBaseUrl(mockWebServer: MockWebServer): HttpUrl {
        var url: HttpUrl? = null
        val thread = Thread(Runnable {
            url = mockWebServer.url("/")
        })
        thread.start()
        thread.join()
        return url!!
    }
}