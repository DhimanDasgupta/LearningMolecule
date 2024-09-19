package com.dhimandasgupta.learningmolecule

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dhimandasgupta.learningmolecule.ui.theme.Home
import com.dhimandasgupta.molecule.presenter.CounterPresenter
import com.dhimandasgupta.molecule.presenter.NetworkPresenter
import com.dhimandasgupta.molecule.presenter.RunningTimeUiState
import com.dhimandasgupta.state.machines.CounterStateMachine
import com.dhimandasgupta.state.machines.NetworkStateMachine
import kotlinx.serialization.Serializable

// Parent Graph
interface NavigationGraph

// All Graphs are here - Child graph
@Serializable
object HomeGraph : NavigationGraph

// Parent Route
interface NavigationRoute

// All the composable for Home Graph
@Serializable
object HomeRoute : NavigationRoute

internal fun NavGraphBuilder.homeGraph(
    context: Context,
    windowSizeClass: WindowSizeClass,
    runningUiState: RunningTimeUiState
) = navigation<HomeGraph>(startDestination = HomeRoute) {
    // Home Route
    homeRoute(
        context = context,
        windowSizeClass = windowSizeClass,
        runningUiState = runningUiState
    )
}

private fun NavGraphBuilder.homeRoute(
    context: Context,
    windowSizeClass: WindowSizeClass,
    runningUiState: RunningTimeUiState
) = composable<HomeRoute> {
    val networkStateMachine = remember(key1 = Unit) {
        NetworkStateMachine(context = context)
    }
    val counterStateMachine = remember(key1 = Unit) {
        CounterStateMachine()
    }
    val networkPresenter = remember(key1 = Unit) {
        NetworkPresenter(networkStateMachine)
    }
    val counterPresenter = remember(key1 = Unit) {
        CounterPresenter(counterStateMachine)
    }

    Home(
        windowSizeClass = windowSizeClass,
        runningTimeUiState = runningUiState,
        connectedState = networkPresenter.uiModel(),
        counterState = counterPresenter.uiModel()
    ) { counterEvent ->
        counterPresenter.processEvent(counterEvent)
    }
}
