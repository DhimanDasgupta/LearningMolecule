package com.dhimandasgupta.learningmolecule.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun Dial(windowSizeClass: WindowSizeClass) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.Magenta))
}
