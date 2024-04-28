package com.dhimandasgupta.molecule.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dhimandasgupta.common.android.ConnectionState
import com.dhimandasgupta.state.machines.NetworkStateMachine

class NetworkPresenter(
    private val networkStateMachines: NetworkStateMachine
) {
    @Composable
    fun uiModel(): ConnectionState {
        var connectionState by remember {
            mutableStateOf(networkStateMachines.immediateConnectedState())
        }

        // Receives the State from the StateMachine
        LaunchedEffect(Unit) {
            networkStateMachines.state.collect { connectionStateValue ->
                connectionState = connectionStateValue
            }
        }

        return connectionState
    }
}