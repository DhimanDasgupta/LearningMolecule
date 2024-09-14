package com.dhimandasgupta.state.machines

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import com.freeletics.flowredux.dsl.FlowReduxStateMachine as StateMachine

@Immutable
sealed interface RunningTimeState {
    val runningTime: Long
}

data class Started(override val runningTime: Long = 0L): RunningTimeState

sealed interface RunningTimeEvent

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTimeStateMachine : StateMachine<RunningTimeState, RunningTimeEvent>(initialState = defaultRunningTimeState()) {
    init {
        spec {
            inState<Started> {
                collectWhileInState(time()) { time, state ->
                    state.override { state.snapshot.copy(runningTime = time) }
                }
            }
        }
    }

    private fun time(delay: Long = 1000L) = flow {
        var time = 0L
        while (true) {
            emit(time)
            delay(delay)
            time += delay
        }
    }
    companion object  {
        fun defaultRunningTimeState(): RunningTimeState = Started()
    }
}
