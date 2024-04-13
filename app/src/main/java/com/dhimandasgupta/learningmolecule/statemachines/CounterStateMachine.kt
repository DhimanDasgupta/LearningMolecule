package com.dhimandasgupta.learningmolecule.statemachines

import androidx.compose.runtime.Immutable
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Immutable
sealed interface CounterState {
    val counter: Int
}

data class NotInitialized(override var counter: Int = 0): CounterState
data class Increasing(override val counter: Int): CounterState
data class Decreasing(override val counter: Int): CounterState

sealed interface CounterEvent
data object Empty: CounterEvent
data object Increase: CounterEvent
data object Decrease: CounterEvent

@OptIn(ExperimentalCoroutinesApi::class)
class CounterStateMachine: FlowReduxStateMachine<CounterState, CounterEvent>(initialState = defaultCounterState()) {
    init {
        spec {
            // Definition of Not-Initialized state
            inState<NotInitialized> {
                on<Increase> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<Decrease> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<Empty> { _, state ->
                    state.override { defaultCounterState() }
                }
            }

            // Definition of Increasing state
            inState<Increasing> {
                on<Increase> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<Decrease> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<Empty> { _, state ->
                    state.override { defaultCounterState() }
                }
            }

            // Definition of Decreasing state
            inState<Decreasing> {
                on<Increase> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<Decrease> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<Empty> { _, state ->
                    state.override { defaultCounterState() }
                }
            }
        }
    }
    companion object {
        fun defaultCounterState(): CounterState = NotInitialized()
    }
}