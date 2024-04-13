package com.dhimandasgupta.learningmolecule.presenters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dhimandasgupta.learningmolecule.ConnectionState
import com.dhimandasgupta.learningmolecule.statemachines.NetworkStateMachine

class NetworkPresenter(
    private val networkStateMachines: NetworkStateMachine
) {
    @Composable
    fun uiModel(): ConnectionState {
        var connectionState by remember {
            mutableStateOf(networkStateMachines.immediateConnectedState())
        }

        LaunchedEffect(Unit) {
            networkStateMachines.state.collect {
                connectionState = it
            }
        }

        return connectionState
    }
}