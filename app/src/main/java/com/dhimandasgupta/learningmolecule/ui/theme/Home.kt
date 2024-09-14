package com.dhimandasgupta.learningmolecule.ui.theme

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.common.android.ConnectionState
import com.dhimandasgupta.common.compose.LoadingSpinner
import com.dhimandasgupta.common.compose.NavigationDial
import com.dhimandasgupta.molecule.presenter.CounterPresenter
import com.dhimandasgupta.molecule.presenter.NetworkPresenter
import com.dhimandasgupta.state.machines.CounterStateMachine
import com.dhimandasgupta.state.machines.DecreaseEvent
import com.dhimandasgupta.state.machines.IncreaseEvent
import com.dhimandasgupta.state.machines.NetworkStateMachine

@Composable
internal fun Home(windowSizeClass: WindowSizeClass) {
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

            NavigationDial(
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
        ) {
            IncrementDecrementButton(
                text = incrementText,
                onClick = { counterPresenter.processEvent(IncreaseEvent) }
            )

            IncrementDecrementButton(
                text = decrementText,
                onClick = { counterPresenter.processEvent(DecreaseEvent) }
            )
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
            .padding(16.dp)
            .wrapContentSize(align = Alignment.Center),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            style = typography.displaySmall,
            textAlign = TextAlign.Center
        )
    }
}

private fun WindowSizeClass.getButtonText(
    text: String
): String = if (widthSizeClass == WindowWidthSizeClass.Compact || heightSizeClass == WindowHeightSizeClass.Compact) {
    text
} else text + text