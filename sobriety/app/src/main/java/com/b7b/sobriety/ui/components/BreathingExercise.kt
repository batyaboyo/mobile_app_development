package com.b7b.sobriety.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.PrimaryDark
import kotlinx.coroutines.delay

@Composable
fun BreathingExercise() {
    var isRunning by remember { mutableStateOf(false) }
    val readyText = stringResource(R.string.ready)
    val inhaleText = stringResource(R.string.inhale)
    val holdText = stringResource(R.string.hold)
    val exhaleText = stringResource(R.string.exhale)
    
    var phase by remember { mutableStateOf(readyText) }
    
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                // Inhale (4s)
                phase = inhaleText
                scale.animateTo(1.6f, animationSpec = tween(4000, easing = LinearOutSlowInEasing))
                
                // Hold (7s)
                phase = holdText
                delay(7000)
                
                // Exhale (8s)
                phase = exhaleText
                scale.animateTo(1f, animationSpec = tween(8000, easing = FastOutLinearInEasing))
            }
        } else {
            phase = readyText
            scale.snapTo(1f)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .scale(scale.value)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Primary, PrimaryDark)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                phase,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier.width(200.dp)
        ) {
            Text(if (isRunning) stringResource(R.string.stop_exercise) else stringResource(R.string.start_exercise))
        }
    }
}
