package com.theword.app.ui.prayer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theword.app.data.embedded.PrayerData
import com.theword.app.domain.model.Prayer
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun PrayerScreen() {
    val isEvening = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 17
    var showEvening by remember { mutableStateOf(isEvening) }
    var currentIndex by remember { mutableStateOf(0) }

    val prayers = if (showEvening) PrayerData.evening else PrayerData.morning
    val prayer = prayers[currentIndex % prayers.size]

    // Timer state
    var timerRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    val totalSeconds = 120 // 2 minutes

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (elapsedSeconds < totalSeconds) {
                delay(1000)
                elapsedSeconds++
            }
            timerRunning = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Prayer Time", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Morning/Evening toggle
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = !showEvening,
                onClick = { showEvening = false; currentIndex = 0; timerRunning = false; elapsedSeconds = 0 },
                label = { Text("☀️ Morning") }
            )
            FilterChip(
                selected = showEvening,
                onClick = { showEvening = true; currentIndex = 0; timerRunning = false; elapsedSeconds = 0 },
                label = { Text("🌙 Evening") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prayer card
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(prayer.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "\"${prayer.verse}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "— ${prayer.verseRef}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(prayer.text, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(prayer.closing, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Timer
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
            val progress = if (totalSeconds > 0) elapsedSeconds.toFloat() / totalSeconds else 0f
            val arcColor = MaterialTheme.colorScheme.primary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant

            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                val arcSize = size.minDimension - strokeWidth
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcSize, arcSize),
                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = arcColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcSize, arcSize),
                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                )
            }

            val minutes = (totalSeconds - elapsedSeconds) / 60
            val seconds = (totalSeconds - elapsedSeconds) % 60
            Text(
                String.format("%d:%02d", minutes, seconds),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer controls
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(
                onClick = {
                    if (timerRunning) { timerRunning = false } else { elapsedSeconds = 0; timerRunning = true }
                }
            ) {
                Text(if (timerRunning) "⏸ Pause" else "▶ Start Timer")
            }
            if (elapsedSeconds > 0) {
                OutlinedButton(onClick = { timerRunning = false; elapsedSeconds = 0 }) {
                    Text("Reset")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next prayer
        OutlinedButton(onClick = { currentIndex = (currentIndex + 1) % prayers.size }) {
            Text("Next Prayer")
        }
    }
}
