package com.michaelbukachi.flightschedules

import android.content.Context
import androidx.lifecycle.*
import com.michaelbukachi.flightschedules.di.appModules
import com.michaelbukachi.flightschedules.di.dataModules
import com.michaelbukachi.flightschedules.di.domainModules
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module


@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class DependenciesRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                val testContext = module {
                    single { mock<Context>() }
                }
                val mainThreadSurrogate = newSingleThreadContext("UI thread")
                Dispatchers.setMain(mainThreadSurrogate)
                startKoin { modules(listOf(testContext, appModules, domainModules, dataModules)) }
                base?.evaluate()
                Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
                mainThreadSurrogate.close()
                stopKoin()
            }
        }
    }
}

class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}