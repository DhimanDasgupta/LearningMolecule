package com.dhimandasgupta.learningmolecule.graphs

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dhimandasgupta.learningmolecule.routes.Dial
import kotlinx.serialization.Serializable

@Serializable
object DialGraph : NavigationGraph

@Serializable
object DialRoute : NavigationRoute

internal fun NavGraphBuilder.dialGraph(windowSizeClass: WindowSizeClass) =
    navigation<DialGraph>(startDestination = DialRoute) {
        // Dial Route
        dialRoute(
            windowSizeClass = windowSizeClass,
        )
    }

private fun NavGraphBuilder.dialRoute(windowSizeClass: WindowSizeClass) =
    composable<DialRoute> {
        Dial(
            windowSizeClass = windowSizeClass,
        )
    }
