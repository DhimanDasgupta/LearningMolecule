package com.dhimandasgupta.learningmolecule

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.learningmolecule.presenters.CounterPresenter
import com.dhimandasgupta.learningmolecule.presenters.NetworkPresenter
import com.dhimandasgupta.learningmolecule.statemachines.CounterStateMachine
import com.dhimandasgupta.learningmolecule.statemachines.Decrease
import com.dhimandasgupta.learningmolecule.statemachines.Increase
import com.dhimandasgupta.learningmolecule.statemachines.NetworkStateMachine
import com.dhimandasgupta.learningmolecule.ui.theme.LearningMoleculeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .systemBarsPadding()
                        .displayCutoutPadding()
                        .fillMaxSize()
                        .background(colorScheme.primary.copy(alpha = 0.5f)),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        val networkState = networkPresenter.uiModel()
                        when (networkState) {
                            ConnectionState.Available -> Text(text = "Connected", color = Color.Green, style = typography.headlineMedium)
                            ConnectionState.Unavailable ->  Text(text = "Disconnected", color = Color.Red, style = typography.headlineLarge)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        val activity = LocalContext.current as Activity
                        val windowWidthSizeClass = calculateWindowSizeClass(activity = activity)
                        Text(text = "${windowWidthSizeClass.widthSizeClass}, ${windowWidthSizeClass.heightSizeClass}", color = Color.DarkGray, style = typography.labelMedium)
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

                    val increment = remember { { counterPresenter.processEvent(Increase) } }
                    val decrement = remember { { counterPresenter.processEvent(Decrease) } }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                    ) {
                        IncrementDecrementButton(
                            text = "++",
                            onClick = { increment() }
                        )

                        IncrementDecrementButton(
                            text = "--",
                            onClick = { decrement() }
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
    val textToBeDrawn = remember { text }

    Button(
        modifier = Modifier
            .width(156.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = textToBeDrawn,
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



