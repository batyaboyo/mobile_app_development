package com.theword.app.ui.prayer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theword.app.data.embedded.PrayerData
import com.theword.app.domain.model.Prayer
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun PrayerScreen(initialIsEvening: Boolean? = null, initialIndex: Int? = null, onBack: () -> Unit = {}) {
    val isEvening = initialIsEvening ?: (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 17)
    var showEvening by remember { mutableStateOf(isEvening) }
    var currentIndex by remember { mutableStateOf(initialIndex ?: 0) }

    val prayers = if (showEvening) PrayerData.evening else PrayerData.morning
    val prayer = prayers[currentIndex % prayers.size]

    // Prayer state
    var isPrayed by remember(currentIndex, showEvening) { mutableStateOf(false) }
    
    // Fireworks/Particle state
    val particles = remember { mutableStateListOf<Particle>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(particles.isNotEmpty()) {
        if (particles.isNotEmpty()) {
            while (particles.isNotEmpty()) {
                withFrameNanos { frameTime ->
                    val toRemove = mutableListOf<Int>()
                    for (i in particles.indices) {
                        val p = particles[i]
                        val updated = p.copy(
                            x = p.x + p.vx,
                            y = p.y + p.vy,
                            vy = p.vy + 0.2f, // gravity
                            alpha = p.alpha - 0.02f
                        )
                        if (updated.alpha <= 0) {
                            toRemove.add(i)
                        } else {
                            particles[i] = updated
                        }
                    }
                    toRemove.reversed().forEach { particles.removeAt(it) }
                }
            }
        }
    }

    fun triggerFireworks(centerX: Float, centerY: Float) {
        val colors = listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFFFF4500), // OrangeRed
            Color(0xFF00BFFF), // DeepSkyBlue
            Color(0xFFADFF2F), // GreenYellow
            Color(0xFFFF69B4)  // HotPink
        )
        repeat(30) {
            val angle = Math.random() * 2 * Math.PI
            val speed = Math.random() * 15 + 5
            particles.add(
                Particle(
                    x = centerX,
                    y = centerY,
                    vx = (Math.cos(angle) * speed).toFloat(),
                    vy = (Math.sin(angle) * speed).toFloat(),
                    color = colors.random(),
                    alpha = 1f,
                    size = (Math.random() * 10 + 5).toFloat()
                )
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Prayer Time", 
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Morning/Evening toggle
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = !showEvening,
                onClick = { showEvening = false; currentIndex = 0 },
                label = { Text("☀️ Morning") }
            )
            FilterChip(
                selected = showEvening,
                onClick = { showEvening = true; currentIndex = 0 },
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

        Spacer(modifier = Modifier.height(32.dp))
        
        // Amen button with fireworks overlay
        Box(contentAlignment = Alignment.Center) {
            if (particles.isNotEmpty()) {
                Canvas(modifier = Modifier.size(300.dp)) {
                    particles.forEach { p ->
                        drawCircle(
                            color = p.color,
                            radius = p.size,
                            center = Offset(p.x, p.y),
                            alpha = p.alpha
                        )
                    }
                }
            }
            
            Button(
                onClick = { 
                    if (!isPrayed) {
                        isPrayed = true
                        triggerFireworks(0f, 0f) // Center of the Box
                    }
                },
                modifier = Modifier
                    .height(64.dp)
                    .width(180.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPrayed) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary,
                    contentColor = if (isPrayed) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    if (isPrayed) "🙏 Blessed" else "✨ Amen",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isPrayed) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "You have prayed this prayer. May it be so.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val alpha: Float,
    val size: Float
)

