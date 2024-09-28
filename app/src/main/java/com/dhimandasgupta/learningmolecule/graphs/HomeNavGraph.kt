package com.dhimandasgupta.learningmolecule.graphs

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dhimandasgupta.learningmolecule.routes.Home
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
    windowSizeClass: WindowSizeClass,
    runningUiState: RunningTimeUiState,
    navigateToDial: () -> Unit = {},
) = navigation<HomeGraph>(startDestination = HomeRoute) {
    // Home Route
    homeRoute(
        windowSizeClass = windowSizeClass,
        runningUiState = runningUiState,
        navigateToDial = navigateToDial,
    )
}

private fun NavGraphBuilder.homeRoute(
    windowSizeClass: WindowSizeClass,
    runningUiState: RunningTimeUiState,
    navigateToDial: () -> Unit = {},
) = composable<HomeRoute> {
    // Application Context if needed by individual Navigation Graph or Composable
    val context = LocalContext.current.applicationContext

    val networkStateMachine =
        remember(key1 = Unit) {
            NetworkStateMachine(context = context)
        }
    val counterStateMachine =
        remember(key1 = Unit) {
            CounterStateMachine()
        }
    val networkPresenter =
        remember(key1 = Unit) {
            NetworkPresenter(networkStateMachine)
        }
    val counterPresenter =
        remember(key1 = Unit) {
            CounterPresenter(counterStateMachine)
        }

    Home(
        windowSizeClass = windowSizeClass,
        runningTimeUiState = runningUiState,
        connectedState = networkPresenter.uiModel(),
        counterState = counterPresenter.uiModel(),
        navigateToDial = navigateToDial,
    ) { counterEvent ->
        counterPresenter.processEvent(counterEvent)
    }
}
