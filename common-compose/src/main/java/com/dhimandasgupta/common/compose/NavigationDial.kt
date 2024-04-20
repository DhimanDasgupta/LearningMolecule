package com.dhimandasgupta.common.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NavigationDial(
    modifier: Modifier
) {
    val numberOfItems by remember { mutableIntStateOf(10) }

    Box(
        modifier = modifier
            .drawBehind {
                drawItemsOnCircle(numberOfItems)
            }
    )
}

private fun DrawScope.drawItemsOnCircle(itemCount: Int) {
    val radius = size.minDimension / 2
    val center = size.center
    val angleSpacing = 360f / itemCount

    drawCircle(Color.LightGray, radius, center) // Optional background circle

    for (i in 0 until itemCount) {
        val angle = (i * angleSpacing).toDouble().toRadians()
        val itemOffset = Offset(
            x = (center.x + radius * cos(angle) - 15).toFloat(), // Adjust 15 for item size/spacing
            y = (center.y + radius * sin(angle) - 15).toFloat()
        )

        drawRect(
            color = when (i % itemCount) {
                0 -> Color.Blue
                1 -> Color.Red
                2 -> Color.DarkGray
                3 -> Color.Magenta
                else -> Color.Cyan
            },
            size = Size(30f, 30f),
            topLeft = itemOffset,
            style = Stroke(2f)
        )
    }
}

// Extension to convert to radians
private fun Double.toRadians() = (this * PI / 180.0)