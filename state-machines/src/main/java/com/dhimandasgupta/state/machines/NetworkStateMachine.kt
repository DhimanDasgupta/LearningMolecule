package com.dhimandasgupta.state.machines

import android.app.Application
import android.content.Context
import com.dhimandasgupta.common.android.currentConnectivityState
import com.dhimandasgupta.common.android.observeConnectivityAsFlow
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE =
    "Context passed in NetworkStateMachines must be Application Context"

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkStateMachine(
    private val context: Context
) : FlowReduxStateMachine<com.dhimandasgupta.common.android.ConnectionState, Unit>(initialState = defaultConnectionState()) {
    init {
        if (context !is Application) throw IllegalArgumentException(
            ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE
        )

        spec {
            inState<com.dhimandasgupta.common.android.ConnectionState.Unavailable> {
                collectWhileInState(context.observeConnectivityAsFlow()) { connectionState, state ->
                    state.override { connectionState }
                }
            }

            inState<com.dhimandasgupta.common.android.ConnectionState.Available> {
                collectWhileInState(context.observeConnectivityAsFlow()) { connectionState, state ->
                    state.override { connectionState }
                }
            }
        }
    }

    fun immediateConnectedState(): com.dhimandasgupta.common.android.ConnectionState = context.currentConnectivityState

    companion object {
        fun defaultConnectionState(): com.dhimandasgupta.common.android.ConnectionState = com.dhimandasgupta.common.android.ConnectionState.Unavailable
    }
}