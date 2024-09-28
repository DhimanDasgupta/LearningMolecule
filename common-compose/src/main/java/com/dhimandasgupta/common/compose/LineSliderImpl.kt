package com.dhimandasgupta.common.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun LineSliderImpl(modifier: Modifier = Modifier) {
    var value by remember { mutableFloatStateOf(.4f) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var steps by remember { mutableIntStateOf(10) }
        LineSlider(
            value = value,
            onValueChange = {
                value = it
            },
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 400.dp),
            steps = steps,
            thumbDisplay = { it.toString().take(4) },
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { steps = (steps - 1).coerceAtLeast(0) },
            ) {
                Text("-", color = colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
            }
            Text("steps: $steps", color = colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
            IconButton(
                onClick = { steps = (steps + 1).coerceAtMost(50) },
            ) {
                Text("+", color = colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    steps: Int = 0,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbDisplay: (Float) -> String = { "" },
) {
    val interaction = remember { MutableInteractionSource() }
    val isDragged by interaction.collectIsDraggedAsState()
    val density = LocalDensity.current
    val offset by animateFloatAsState(
        targetValue = with(density) { if (isDragged) 36.dp.toPx() else 0.dp.toPx() },
        animationSpec =
            spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = 0.65f,
            ),
        label = "offsetAnimation",
    )

    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
            ),
        label = "animatedValue",
    )

    Slider(
        value = animatedValue,
        onValueChange = onValueChange,
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        interactionSource = interaction,
        thumb = {},
        track = { sliderState ->

            val fraction by remember {
                derivedStateOf {
                    (animatedValue - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                }
            }

            var width by remember { mutableIntStateOf(0) }

            Box(
                Modifier
                    .clearAndSetSemantics { }
                    .height(64.dp)
                    .fillMaxWidth()
                    .onSizeChanged { width = it.width },
            ) {
                Box(
                    Modifier
                        .zIndex(10f)
                        .align(Alignment.CenterStart)
                        .offset {
                            IntOffset(
                                lerp(
                                    start = (-32).dp.toPx(),
                                    end = width - 32.dp.toPx(),
                                    t = fraction,
                                ).roundToInt(),
                                0,
                            )
                        }.offset {
                            IntOffset(0, (-offset).roundToInt())
                        }.size(64.dp)
                        .padding(10.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                        ).background(
                            color = colorScheme.surface,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        thumbDisplay(animatedValue),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onSurface,
                    )
                }

                val strokeColor = colorScheme.surface
                val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .drawWithCache {
                            onDrawBehind {
                                scale(
                                    scaleY = 1f,
                                    scaleX = if (isLtr) 1f else -1f,
                                ) {
                                    drawSliderPath(
                                        fraction = fraction,
                                        offset = offset,
                                        color = strokeColor,
                                        steps = sliderState.steps,
                                    )
                                }
                            }
                        },
                )
            }
        },
    )
}

fun DrawScope.drawSliderPath(
    fraction: Float,
    offset: Float,
    color: Color,
    steps: Int,
) {
    val path = Path()
    val activeWidth = size.width * fraction
    val midHeight = size.height / 2
    val topOfTheBump = midHeight - (offset.coerceAtLeast(0f))
    val beyondBound = size.width * 2
    val ramp = 72.dp.toPx()

    path.moveTo(
        x = beyondBound,
        y = midHeight,
    )

    path.lineTo(
        x = activeWidth + ramp,
        y = midHeight,
    )

    path.cubicTo(
        x1 = activeWidth + (ramp / 2),
        y1 = midHeight,
        x2 = activeWidth + (ramp / 2),
        y2 = topOfTheBump,
        x3 = activeWidth,
        y3 = topOfTheBump,
    )

    path.cubicTo(
        x1 = activeWidth - (ramp / 2),
        y1 = topOfTheBump,
        x2 = activeWidth - (ramp / 2),
        y2 = midHeight,
        x3 = activeWidth - ramp,
        y3 = midHeight,
    )

    path.lineTo(
        x = -beyondBound,
        y = midHeight,
    )

    val variation = .1f
    path.lineTo(
        x = -beyondBound,
        y = midHeight + variation,
    )
    path.lineTo(
        x = activeWidth - ramp,
        y = midHeight + variation,
    )

    path.cubicTo(
        x1 = activeWidth - (ramp / 2),
        y1 = midHeight + variation,
        x2 = activeWidth - (ramp / 2),
        y2 = topOfTheBump + variation,
        x3 = activeWidth,
        y3 = topOfTheBump + variation,
    )

    path.cubicTo(
        x1 = activeWidth + (ramp / 2),
        y1 = topOfTheBump + variation,
        x2 = activeWidth + (ramp / 2),
        y2 = midHeight + variation,
        x3 = activeWidth + ramp,
        y3 = midHeight + variation,
    )
    path.lineTo(
        x = size.width * 2f,
        y = midHeight + variation,
    )

    val exclude =
        Path().apply {
            addRect(
                Rect(
                    left = -beyondBound,
                    top = -beyondBound,
                    right = 0f,
                    bottom = beyondBound,
                ),
            )
            addRect(
                Rect(
                    left = size.width,
                    top = -beyondBound,
                    right = beyondBound,
                    bottom = beyondBound,
                ),
            )
        }

    val trimmedPath = Path()
    trimmedPath.op(path, exclude, PathOperation.Difference)

    val pathMeasure = PathMeasure()
    pathMeasure.setPath(trimmedPath, false)

    val graduations = steps + 1
    for (i in 0..graduations) {
        val pos = pathMeasure.getPosition(((i / graduations.toFloat()) * .5f) * pathMeasure.length)
        val height = 10f
        when (i) {
            0, graduations ->
                drawCircle(
                    color = color,
                    radius = 10f,
                    center = pos,
                )
            else ->
                drawLine(
                    strokeWidth = if (pos.x < activeWidth) 4f else 2f,
                    color = color,
                    start = pos + Offset(0f, height),
                    end = pos + Offset(0f, -height),
                )
        }
    }

    val drawTrimmedPath: (Color) -> Unit = { pathColor ->
        drawPath(
            trimmedPath,
            pathColor,
            style =
                Stroke(
                    width = 10f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
        )
    }
    clipRect(
        left = -beyondBound,
        top = -beyondBound,
        bottom = beyondBound,
        right = activeWidth,
    ) {
        drawTrimmedPath(color)
    }
    clipRect(
        left = activeWidth,
        top = -beyondBound,
        bottom = beyondBound,
        right = beyondBound,
    ) {
        drawTrimmedPath(color.copy(alpha = .2f))
    }
}

private fun lerp(
    start: Float,
    end: Float,
    t: Float,
): Float = start + t * (end - start)
