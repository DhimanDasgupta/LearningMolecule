package com.dhimandasgupta.learningmolecule

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.molecule.presenter.RunningTimePresenter
import com.dhimandasgupta.state.machines.RunningTimeStateMachine


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
        homeGraph(
            context = context,
            windowSizeClass = windowSizeClass,
            runningUiState = runningUiState
        )
    }
}