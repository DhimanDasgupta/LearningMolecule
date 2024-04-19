package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableSharedFlow

class CounterPresenter(
    private val counterStateMachine: com.dhimandasgupta.state.machines.CounterStateMachine
) {
    private val events = MutableSharedFlow<com.dhimandasgupta.state.machines.CounterEvent>(extraBufferCapacity = 1)

    @Composable
    fun uiModel(): com.dhimandasgupta.state.machines.CounterState {
        var counterState by remember {
            mutableStateOf(com.dhimandasgupta.state.machines.CounterStateMachine.defaultCounterState())
        }

        // Receives the State from the StateMachine
        LaunchedEffect(Unit) {
            counterStateMachine.state.collect { currentState ->
                counterState = currentState
            }
        }

        // Send the Events to the State Machine through Actions
        LaunchedEffect(Unit) {
            events.collect { counterEvent ->
                counterStateMachine.dispatch(counterEvent)
            }
        }

        return counterState
    }

    fun processEvent(event: com.dhimandasgupta.state.machines.CounterEvent) {
        events.tryEmit(event)
    }
}