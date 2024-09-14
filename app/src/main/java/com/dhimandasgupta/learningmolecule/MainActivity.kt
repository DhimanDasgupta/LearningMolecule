package com.dhimandasgupta.learningmolecule

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import com.dhimandasgupta.learningmolecule.ui.theme.LearningMoleculeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val activity = LocalContext.current as Activity
            val windowSizeClass = calculateWindowSizeClass(activity = activity)

            LearningMoleculeTheme { AppNavigation(windowSizeClass) }
        }
    }
}
