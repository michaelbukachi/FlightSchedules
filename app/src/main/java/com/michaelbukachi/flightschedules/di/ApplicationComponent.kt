package com.michaelbukachi.flightschedules.di

import android.content.Context
import com.michaelbukachi.flightschedules.ui.selection.SelectionViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UrlModule::class, DataModule::class, DomainModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun selectionViewModelFactory(): ViewModelFactory<SelectionViewModel>
}