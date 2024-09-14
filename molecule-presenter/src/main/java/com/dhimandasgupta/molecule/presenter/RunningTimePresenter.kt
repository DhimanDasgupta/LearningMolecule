package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dhimandasgupta.state.machines.RunningTimeStateMachine
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
        var runningTimeUiState by remember(key1 = Unit) {
            mutableStateOf(defaultRunningTimeUiState())
        }

        // Receives the State from the StateMachine
        LaunchedEffect(key1 = Unit) {
            runningTimeStateMachine.state.collect { currentState ->
                val duration = currentState.runningTime.toDuration(DurationUnit.MILLISECONDS)
                duration.toComponents { hours, minutes, seconds, _ ->
                    runningTimeUiState = RunningTimeUiState(
                        formattedTime = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
                    )
                }
            }
        }

        return runningTimeUiState
    }
}