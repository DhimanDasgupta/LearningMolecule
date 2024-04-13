package com.dhimandasgupta.learningmolecule.presenters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dhimandasgupta.learningmolecule.statemachines.CounterEvent
import com.dhimandasgupta.learningmolecule.statemachines.CounterState
import com.dhimandasgupta.learningmolecule.statemachines.CounterStateMachine
import kotlinx.coroutines.flow.MutableSharedFlow

class CounterPresenter(
    private val counterStateMachine: CounterStateMachine
) {
    private val events = MutableSharedFlow<CounterEvent>(extraBufferCapacity = 1)

    @Composable
    fun uiModel(): CounterState {
        var counterState by remember {
            mutableStateOf(CounterStateMachine.defaultCounterState())
        }

        LaunchedEffect(Unit) {
            events.collect { counterEvent ->
                counterStateMachine.dispatch(counterEvent)
            }
        }

        LaunchedEffect(Unit) {
            counterStateMachine.state.collect { currentState ->
                counterState = currentState
            }
        }

        return counterState
    }

    fun processEvent(event: CounterEvent) {
        events.tryEmit(event)
    }
}