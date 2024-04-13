# Getting Network State as a State Machine via FlowRedux

class NetworkStateMachine(
    private val context: Context
) : FlowReduxStateMachine<ConnectionState, Unit>(initialState = defaultConnectionState()) {
    init {
        if (context !is Application) throw IllegalArgumentException("Context passed in NetworkStateMachines must be Application Context")

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

# Observing the State machine and using the as a constructore parameter to the molecule presenter
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

# Getting Counter State as a State Machine via FlowRedux
class CounterStateMachine: FlowReduxStateMachine<CounterState, CounterEvent>(initialState = defaultCounterState()) {
    init {
        spec {
            // ....
        }
    }
    companion object {
        fun defaultCounterState(): CounterState = NotInitialized()
    }
}

# Observing the State machine and using the as a constructore parameter to the molecule presenter, passing the event to the State Machine via the Presenter
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
