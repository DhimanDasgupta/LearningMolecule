package com.dhimandasgupta.learningmolecule.statemachines

import android.app.Application
import android.content.Context
import com.dhimandasgupta.learningmolecule.ConnectionState
import com.dhimandasgupta.learningmolecule.currentConnectivityState
import com.dhimandasgupta.learningmolecule.observeConnectivityAsFlow
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE =
    "Context passed in NetworkStateMachines must be Application Context"

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkStateMachine(
    private val context: Context
) : FlowReduxStateMachine<ConnectionState, Unit>(initialState = defaultConnectionState()) {
    init {
        if (context !is Application) throw IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE)

        spec {
            inState<ConnectionState.Unavailable> {
                collectWhileInState(context.observeConnectivityAsFlow()) { connectionState, state ->
                    state.override { connectionState }
                }
            }

            inState<ConnectionState.Available> {
                collectWhileInState(context.observeConnectivityAsFlow()) { connectionState, state ->
                    state.override { connectionState }
                }
            }
        }
    }

    fun immediateConnectedState(): ConnectionState = context.currentConnectivityState

    companion object {
        fun defaultConnectionState(): ConnectionState = ConnectionState.Unavailable
    }
}