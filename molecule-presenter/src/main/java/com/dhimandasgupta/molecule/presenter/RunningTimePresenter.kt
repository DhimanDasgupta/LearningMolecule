package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import com.dhimandasgupta.state.machines.RunningTimeStateMachine
import io.github.takahirom.rin.produceRetainedState
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Immutable
data class RunningTimeUiState(var formattedTime: String)

class RunningTimePresenter(
    private val runningTimeStateMachine: RunningTimeStateMachine
) {
    companion object {
        fun defaultRunningTimeUiState() = RunningTimeUiState(formattedTime = "00:00:00")
    }

    @Composable
    fun uiModel(): RunningTimeUiState {
        // Receives the State from the StateMachine and pushes the value through the uiModel
        val runningTimeUiState by produceRetainedState(defaultRunningTimeUiState()) {
            runningTimeStateMachine
                .state
                .collect { runningTimeState ->
                    val duration = runningTimeState.runningTime.toDuration(DurationUnit.MILLISECONDS)
                    duration.toComponents { hours, minutes, seconds, _ ->
                        value = RunningTimeUiState(
                            formattedTime = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
                        )
                    }
                }
        }

        return runningTimeUiState
    }
}