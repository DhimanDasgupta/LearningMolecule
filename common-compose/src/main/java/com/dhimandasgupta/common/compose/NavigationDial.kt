package com.dhimandasgupta.common.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NavigationDial(modifier: Modifier) {
    val numberOfItems by remember { mutableIntStateOf(10) }
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 9000,
                        easing = LinearEasing,
                    ),
            ),
        label = "rotationAnimation",
    )

    Box(
        modifier =
            modifier
                .graphicsLayer {
                    rotationZ = angle
                }.drawWithContent {
                    drawItemsOnCircle(numberOfItems)
                },
    )
}

private fun DrawScope.drawItemsOnCircle(
    itemCount: Int,
    rotationAngle: Float = 0f,
) {
    val radius = size.minDimension / 2
    val center = size.center
    val angleSpacing = 360f / itemCount

    val eachRoundedRectSize = radius / 8

    rotate(rotationAngle, center) {
        drawCircle(Color.LightGray.copy(alpha = 0.2f), radius, center) // Optional background circle

        for (i in 0 until itemCount) {
            val angle = (i * angleSpacing).toDouble().toRadians()
            val itemOffset =
                Offset(
                    x = (center.x + radius * cos(angle) - eachRoundedRectSize / 2).toFloat(), // Adjust 15 for item size/spacing
                    y = (center.y + radius * sin(angle) - eachRoundedRectSize / 2).toFloat(),
                )

            drawRoundRect(
                color =
                    when (i % 5) {
                        0 -> Color.Blue
                        1 -> Color.Red
                        2 -> Color.DarkGray
                        3 -> Color.Magenta
                        else -> Color.Green
                    },
                size = Size(eachRoundedRectSize, eachRoundedRectSize),
                topLeft = itemOffset,
                style = Fill,
                cornerRadius =
                    CornerRadius(
                        x = 10f,
                        y = 10f,
                    ),
            )
        }
    }
}

// Extension to convert to radians
private fun Double.toRadians() = (this * PI / 180.0)
