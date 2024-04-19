package com.dhimandasgupta.learningmolecule

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.common.android.ConnectionState
import com.dhimandasgupta.molecule.presenter.CounterPresenter
import com.dhimandasgupta.molecule.presenter.NetworkPresenter
import com.dhimandasgupta.learningmolecule.ui.theme.LearningMoleculeTheme
import com.dhimandasgupta.state.machines.CounterStateMachine
import com.dhimandasgupta.state.machines.NetworkStateMachine

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current.applicationContext
            val networkStateMachine = remember {
                NetworkStateMachine(context = context)
            }
            val counterStateMachine = remember {
                CounterStateMachine()
            }
            val networkPresenter = remember {
                NetworkPresenter(networkStateMachine)
            }
            val counterPresenter = remember {
                CounterPresenter(counterStateMachine)
            }

            LearningMoleculeTheme {
                val activity = LocalContext.current as Activity
                val windowSizeClass = calculateWindowSizeClass(activity = activity)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .systemBarsPadding()
                        .fillMaxSize()
                        .background(colorScheme.primary.copy(alpha = 0.5f)),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        val networkState = networkPresenter.uiModel()
                        Text(
                            text = when(networkState) {
                                ConnectionState.Available -> "Connected"
                                ConnectionState.Unavailable -> "Disconnected"
                            },
                            color = when(networkState) {
                                ConnectionState.Available -> Color.Green
                                ConnectionState.Unavailable -> Color.Red
                            },
                            style = typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "${windowSizeClass.widthSizeClass}, ${windowSizeClass.heightSizeClass}",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            style = typography.labelMedium
                        )
                    }

                    val counterState = counterPresenter.uiModel()

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        LoadingSpinner(
                            modifier = Modifier.size(200.dp)
                        )

                        Text(
                            text = "${counterState.counter}",
                            style = typography.displayLarge
                        )
                    }

                    val incrementText = remember(key1 = windowSizeClass) {
                        windowSizeClass.getButtonText("+")
                    }

                    val decrementText = remember(key1 = windowSizeClass) {
                        windowSizeClass.getButtonText("-")
                    }

                    val incrementEvent = remember { { counterPresenter.processEvent(com.dhimandasgupta.state.machines.IncreaseEvent) } }
                    val decrementEvent = remember { { counterPresenter.processEvent(com.dhimandasgupta.state.machines.DecreaseEvent) } }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                    ) {
                        IncrementDecrementButton(
                            text = incrementText,
                            onClick = { incrementEvent() }
                        )

                        IncrementDecrementButton(
                            text = decrementText,
                            onClick = { decrementEvent() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IncrementDecrementButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .width(156.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            style = typography.displayLarge
        )
    }
}

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    val animation = rememberInfiniteTransition(label = "rotation")
    val rotation = animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20_000)
        ), label = "rotation"
    )
    Box(modifier = modifier.graphicsLayer { rotationZ = rotation.value }) {
        GradientCircle(color = Color(0xff1AB9D5))
        GradientCircle(color = Color(0xff1A74D5), delay = 200)
        GradientCircle(color = Color(0xff1A4FD5), delay = 400)
    }
}

@Composable
fun GradientCircle(
    modifier: Modifier = Modifier,
    color: Color,
    delay: Int = 0,
) {
    val animation = rememberInfiniteTransition(label = "infinite_animation")
    val rotation = animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3_000, easing = LinearEasing),
            initialStartOffset = StartOffset(delay)
        ), label = "gradientCircleRotation"
    )
    Box(
        modifier = modifier
            .graphicsLayer { rotationX = rotation.value; cameraDistance = 100000f }
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color, Color.Transparent),
                        center = Offset.Zero,
                        radius = size.width,
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color, Color.Transparent),
                        center = Offset.Zero,
                        radius = size.width * 1.5f,
                    ),
                    style = Stroke(width = 1f)
                )
            }
    )
}

private fun WindowSizeClass.getButtonText(
    text: String
): String = if (widthSizeClass == WindowWidthSizeClass.Compact || heightSizeClass == WindowHeightSizeClass.Compact) {
    text
} else text + text


