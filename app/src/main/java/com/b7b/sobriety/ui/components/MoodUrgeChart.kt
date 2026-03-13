package com.b7b.sobriety.ui.components

import androidx.compose.foundation.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success

@Composable
fun MoodUrgeChart(checkIns: List<CheckIn>) {
    val sortedCheckIns = checkIns.takeLast(7).sortedBy { it.date }
    
    if (sortedCheckIns.size < 2) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Log at least 2 days to see trends", style = MaterialTheme.typography.bodySmall)
        }
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            val width = size.width
            val height = size.height
            val spacing = width / (sortedCheckIns.size - 1)

            val moodPoints = sortedCheckIns.mapIndexed { index, checkIn ->
                val moodValue = when (checkIn.mood) {
                    "😊" -> 4f
                    "🙂" -> 3f
                    "😐" -> 2f
                    "😔" -> 1f
                    else -> 0f
                }
                Offset(index * spacing, height - (moodValue / 4f * height))
            }

            val urgePoints = sortedCheckIns.mapIndexed { index, checkIn ->
                val urgeValue = when (checkIn.urge) {
                    "High" -> 1f
                    "Strong" -> 2f
                    "Mild" -> 3f
                    "None" -> 4f
                    else -> 0f
                }
                Offset(index * spacing, height - (urgeValue / 4f * height))
            }

            // Draw Paths
            drawPath(
                path = Path().apply {
                    moodPoints.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
                },
                color = Success,
                style = Stroke(width = 3.dp.toPx())
            )

            drawPath(
                path = Path().apply {
                    urgePoints.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
                },
                color = Primary,
                style = Stroke(width = 3.dp.toPx())
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem(Success, "Mood")
            LegendItem(Primary, "Urge (Resistance)")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
        Text(label, modifier = Modifier.padding(start = 4.dp), style = MaterialTheme.typography.labelSmall)
    }
}
