package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProgressScreen(quizStats: com.batyaboyo.bibleapp.model.QuizStats, prayerLog: Map<String, Map<String, Boolean>>) {
    val prayerStreak = remember(prayerLog) {
        var streak = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance()
        for (i in 0 until 365) {
            val dateStr = sdf.format(cal.time)
            if (prayerLog.containsKey(dateStr)) {
                streak++
            } else {
                if (i > 0) break
            }
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        streak
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Your Progress", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            HorizontalDivider()
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Prayer Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Current Streak", style = MaterialTheme.typography.labelMedium)
                            Text("$prayerStreak Days", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        Icon(
                            imageVector = Icons.Outlined.SelfImprovement,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Quiz Performance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Accuracy", style = MaterialTheme.typography.labelMedium)
                            val accuracy = if (quizStats.totalQuestions > 0) 
                                (quizStats.correctAnswers * 100 / quizStats.totalQuestions) else 0
                            Text("$accuracy%", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Streak", style = MaterialTheme.typography.labelMedium)
                            Text("${quizStats.streak}", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Best", style = MaterialTheme.typography.labelMedium)
                            Text("${quizStats.bestStreak}", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Total Questions Answered: ${quizStats.totalQuestions}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
