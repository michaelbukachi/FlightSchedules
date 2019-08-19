package com.michaelbukachi.flightschedules.di

import com.michaelbukachi.flightschedules.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

@Module
object UrlModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return BuildConfig.API_BASE_URL.toHttpUrl()
    }
}