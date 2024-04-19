package com.dhimandasgupta.state.machines

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
data object NoEvent: CounterEvent
data object IncreaseEvent: CounterEvent
data object DecreaseEvent: CounterEvent

@OptIn(ExperimentalCoroutinesApi::class)
class CounterStateMachine: FlowReduxStateMachine<CounterState, CounterEvent>(initialState = defaultCounterState()) {
    init {
        spec {
            // Definition of Not-Initialized state
            inState<NotInitialized> {
                on<IncreaseEvent> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<DecreaseEvent> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<NoEvent> { _, state ->
                    state.override { defaultCounterState() }
                }
            }

            // Definition of Increasing state
            inState<Increasing> {
                on<IncreaseEvent> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<DecreaseEvent> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<NoEvent> { _, state ->
                    state.override { defaultCounterState() }
                }
            }

            // Definition of Decreasing state
            inState<Decreasing> {
                on<IncreaseEvent> { _, state ->
                    state.override { Increasing(counter = state.snapshot.counter + 1) }
                }
                on<DecreaseEvent> { _, state ->
                    state.override { Decreasing(counter = state.snapshot.counter - 1) }
                }
                on<NoEvent> { _, state ->
                    state.override { defaultCounterState() }
                }
            }
        }
    }
    companion object {
        fun defaultCounterState(): CounterState = NotInitialized()
    }
}