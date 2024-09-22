package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dhimandasgupta.state.machines.CounterEvent
import com.dhimandasgupta.state.machines.CounterState
import com.dhimandasgupta.state.machines.CounterStateMachine
import com.dhimandasgupta.state.machines.CounterStateMachine.Companion.defaultCounterState
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.MutableSharedFlow

class CounterPresenter(
    private val counterStateMachine: CounterStateMachine
) {
    private val events = MutableSharedFlow<CounterEvent>(extraBufferCapacity = 10)

    @Composable
    fun uiModel(): CounterState {
        var counterState by rememberRetained { mutableStateOf(defaultCounterState()) }

        // Receives the State from the StateMachine
        LaunchedEffect(key1 = Unit) {
            counterStateMachine.state.collect { currentState ->
                counterState = currentState
            }
        }

        // Send the Events to the State Machine through Actions
        LaunchedEffect(key1 = Unit) {
            events.collect { counterEvent ->
                counterStateMachine.dispatch(counterEvent)
            }
        }

        return counterState
    }

    fun processEvent(event: CounterEvent) {
        events.tryEmit(event)
    }
}