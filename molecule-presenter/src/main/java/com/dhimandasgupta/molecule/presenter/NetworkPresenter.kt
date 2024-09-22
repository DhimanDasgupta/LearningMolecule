package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.dhimandasgupta.common.android.ConnectionState
import com.dhimandasgupta.state.machines.NetworkStateMachine
import io.github.takahirom.rin.produceRetainedState

class NetworkPresenter(
    private val networkStateMachines: NetworkStateMachine
) {
    @Composable
    fun uiModel(): ConnectionState {
        // Receives the State from the StateMachine and pushes the value through the uiModel
        val connectionState by produceRetainedState<ConnectionState>(ConnectionState.Unavailable) {
            networkStateMachines
                .state
                .collect { connectionState ->
                    value = connectionState
                }
        }

        return connectionState
    }
}