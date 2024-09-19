package com.dhimandasgupta.learningmolecule

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.molecule.presenter.RunningTimePresenter
import com.dhimandasgupta.state.machines.RunningTimeStateMachine

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
        homeGraph(
            windowSizeClass = windowSizeClass,
            runningUiState = runningUiState
        )
    }
}