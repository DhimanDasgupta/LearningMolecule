package com.dhimandasgupta.learningmolecule

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.learningmolecule.ui.theme.Home
import kotlinx.serialization.Serializable

// All Graphs are here
@Serializable
object HomeGraph

// All the composable for Home Graph
@Serializable
object HomeRoute

@Composable
fun AppNavigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = HomeGraph) {
        // Home Graph
        navigation<HomeGraph>(startDestination = HomeRoute) {
            // Home Route
            composable<HomeRoute> {
                Home(windowSizeClass)
            }
        }
    }
}