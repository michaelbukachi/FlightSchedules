package com.michaelbukachi.flightschedules.di

import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

@Module
object MockUrlModule {
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideMockServer(): MockWebServer {
//        var mockWebServer: MockWebServer? = null
//        val thread = Thread(Runnable {
//            mockWebServer = MockWebServer()
//            mockWebServer?.start()
//        })
//        thread.start()
//        thread.join()
//        return mockWebServer ?: throw NullPointerException()
//    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return "http://localhost:12346".toHttpUrl()
    }
}