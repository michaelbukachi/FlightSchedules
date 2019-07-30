package com.michaelbukachi.flightschedules.di

import com.google.gson.GsonBuilder
import com.michaelbukachi.flightschedules.BuildConfig
import com.michaelbukachi.flightschedules.data.AuthInterceptor
import com.michaelbukachi.flightschedules.data.TimeoutInterceptor
import com.michaelbukachi.flightschedules.data.api.*
import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepo
import com.michaelbukachi.flightschedules.data.repos.FlightSchedulesRepoImpl
import com.michaelbukachi.flightschedules.ui.selection.SelectionViewModel
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    viewModel { SelectionViewModel(get()) }
}

val dataModules = module {
    single {
        val httpInterceptor: Interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        OkHttpClient.Builder()
            .addNetworkInterceptor(TimeoutInterceptor())
            .addNetworkInterceptor(AuthInterceptor())
            .addInterceptor(httpInterceptor)
            .build()
    }

    single {
        GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(AirportResponse::class.java, airportsDeserializer)
            .registerTypeAdapter(ScheduleResponse::class.java, scheduleDeserializer)
            .create()
    }

    factory { BuildConfig.API_BASE_URL.toHttpUrl() }

    single {
        Retrofit.Builder()
            .baseUrl(get<HttpUrl>())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single { ApiService(get()) }

    single<FlightSchedulesRepo> { FlightSchedulesRepoImpl(get(), get()) }
}