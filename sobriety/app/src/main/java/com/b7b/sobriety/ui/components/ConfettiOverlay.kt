package com.b7b.sobriety.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.ui.theme.Warning
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float
)

@Composable
fun ConfettiOverlay(trigger: Boolean, onFinished: () -> Unit) {
    if (!trigger) return

    val particles = remember {
        List(100) {
            Particle(
                x = 0.5f, // percentage
                y = 0.5f,
                vx = Random.nextFloat() * 0.04f - 0.02f,
                vy = Random.nextFloat() * 0.04f - 0.03f,
                color = listOf(Primary, Success, Warning, Color.Magenta, Color.Yellow).random(),
                size = Random.nextFloat() * 10f + 5f
            )
        }
    }

    var frame by remember { mutableIntStateOf(0) }

    LaunchedEffect(trigger) {
        repeat(120) { // ~2 seconds @ 60fps
            delay(16)
            particles.forEach { p ->
                p.x += p.vx
                p.y += p.vy
                p.vy += 0.001f // gravity
            }
            frame++
        }
        onFinished()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEach { p ->
            drawCircle(
                color = p.color,
                radius = p.size,
                center = Offset(p.x * w, p.y * h)
            )
        }
    }
}
