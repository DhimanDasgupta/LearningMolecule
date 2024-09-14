package com.dhimandasgupta.common.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NavigationDial(
    modifier: Modifier
) {
    val numberOfItems by remember { mutableIntStateOf(10) }
    var rotation by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = rotation) {
        delay(16)
        if (rotation == 360f) {
            rotation = 0f
        }
        rotation += 1f
    }

    Box(
        modifier = modifier
            .drawWithContent {
                drawItemsOnCircle(numberOfItems, rotation)
            }
    )
}

private fun DrawScope.drawItemsOnCircle(
    itemCount: Int,
    rotationAngle: Float = 0f
) {
    val radius = size.minDimension / 2
    val center = size.center
    val angleSpacing = 360f / itemCount

    val eachRoundedRectSize = radius / 8

    rotate(rotationAngle, center) {
        drawCircle(Color.LightGray.copy(alpha = 0.2f), radius, center) // Optional background circle

        for (i in 0 until itemCount) {
            val angle = (i * angleSpacing).toDouble().toRadians()
            val itemOffset = Offset(
                x = (center.x + radius * cos(angle) - eachRoundedRectSize / 2).toFloat(), // Adjust 15 for item size/spacing
                y = (center.y + radius * sin(angle) - eachRoundedRectSize / 2).toFloat()
            )

            drawRoundRect(
                color = when (i % 5) {
                    0 -> Color.Blue
                    1 -> Color.Red
                    2 -> Color.DarkGray
                    3 -> Color.Magenta
                    else -> Color.Green
                },
                size = Size(eachRoundedRectSize, eachRoundedRectSize),
                topLeft = itemOffset,
                style = Fill,
                cornerRadius = CornerRadius(
                    x = 10f,
                    y = 10f
                )
            )
        }
    }
}

// Extension to convert to radians
private fun Double.toRadians() = (this * PI / 180.0)