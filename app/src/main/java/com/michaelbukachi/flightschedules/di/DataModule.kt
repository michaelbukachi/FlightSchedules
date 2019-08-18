package com.michaelbukachi.flightschedules.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.michaelbukachi.flightschedules.BuildConfig
import com.michaelbukachi.flightschedules.data.AuthInterceptor
import com.michaelbukachi.flightschedules.data.TimeoutInterceptor
import com.michaelbukachi.flightschedules.data.TokenAuthenticator
import com.michaelbukachi.flightschedules.data.api.AirportResponse
import com.michaelbukachi.flightschedules.data.api.ScheduleResponse
import com.michaelbukachi.flightschedules.data.api.airportsDeserializer
import com.michaelbukachi.flightschedules.data.api.scheduleDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object DataModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return BuildConfig.API_BASE_URL.toHttpUrl()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient(authenticator: TokenAuthenticator): OkHttpClient {
        val httpInterceptor: Interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addNetworkInterceptor(TimeoutInterceptor())
            .addNetworkInterceptor(AuthInterceptor())
            .authenticator(authenticator)
            .addInterceptor(httpInterceptor)
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(AirportResponse::class.java, airportsDeserializer)
            .registerTypeAdapter(ScheduleResponse::class.java, scheduleDeserializer)
            .create()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson, baseUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}