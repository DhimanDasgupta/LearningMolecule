package com.dhimandasgupta.learningmolecule

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.learningmolecule.ui.theme.Home
import com.dhimandasgupta.molecule.presenter.CounterPresenter
import com.dhimandasgupta.molecule.presenter.NetworkPresenter
import com.dhimandasgupta.molecule.presenter.RunningTimePresenter
import com.dhimandasgupta.state.machines.CounterStateMachine
import com.dhimandasgupta.state.machines.NetworkStateMachine
import com.dhimandasgupta.state.machines.RunningTimeStateMachine
import kotlinx.serialization.Serializable

// All Graphs are here
@Serializable
object HomeGraph

// All the composable for Home Graph
@Serializable
object HomeRoute

@Composable
fun AppNavigation(windowSizeClass: WindowSizeClass) {
    // Application Context if needed by individual Navigation Graph or Composable
    val context = LocalContext.current.applicationContext

    // App Specific Data from StateMachine
    val runningTimeStateMachine = remember(key1 = Unit) {
        RunningTimeStateMachine()
    }
    val runningTimePresenter = remember(key1 = Unit) {
        RunningTimePresenter(runningTimeStateMachine)
    }

    val runningUiState = runningTimePresenter.uiModel()

    val navController = rememberNavController()
    NavHost(navController, startDestination = HomeGraph) {
        // Home Graph
        navigation<HomeGraph>(startDestination = HomeRoute) {
            // Home Route
            composable<HomeRoute> {
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
        }
    }
}