package com.dhimandasgupta.learningmolecule

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.learningmolecule.ui.theme.Home
import com.dhimandasgupta.molecule.presenter.RunningTimePresenter
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
                Home(
                    windowSizeClass = windowSizeClass,
                    runningTimeUiState = runningUiState
                )
            }
        }
    }
}