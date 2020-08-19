package com.michaelbukachi.flightschedules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CoroutinesRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {

            override fun evaluate() {
                Dispatchers.setMain(TestCoroutineDispatcher())
                base?.evaluate()
                Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            }
        }
    }
}