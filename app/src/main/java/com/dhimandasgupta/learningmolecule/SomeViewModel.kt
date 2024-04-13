package com.dhimandasgupta.learningmolecule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

/*
class SomeViewModel(
    private val networkPresenter: NetworkPresenter,
    private val counterPresenter: CounterPresenter
) : MoleculeViewModel<CounterEvent, SomeUiState>() {
    @Composable
    override fun models(events: Flow<CounterEvent>): SomeUiState {
        return SomeUiState(
            connectionState = networkPresenter.uiModel(),
            counterState = counterPresenter.uiModel()
        )
    }
}

@Immutable
data class SomeUiState(
    val connectionState: ConnectionState,
    val counterState: CounterState
)*/
