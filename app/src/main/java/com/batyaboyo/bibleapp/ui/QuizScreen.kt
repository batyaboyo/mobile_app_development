package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batyaboyo.bibleapp.model.QuizQuestion

@Composable
fun QuizScreen(questions: List<QuizQuestion>, onResult: (Boolean) -> Unit) {
    val quizSet = remember(questions) { questions.shuffled().take(10) }
    var idx by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var chosen by remember { mutableIntStateOf(-1) }

    if (quizSet.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Quiz data missing.")
        }
        return
    }

    val q = quizSet[idx]

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Daily Quiz", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Question ${idx + 1} of ${quizSet.size} | Score: $score")
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }

        item {
            Text(q.category, style = MaterialTheme.typography.labelMedium)
            Text(q.question, style = MaterialTheme.typography.titleMedium)
        }

        items(q.options.indices.toList()) { optionIdx ->
            val option = q.options[optionIdx]
            val btnColors = if (answered) {
                if (optionIdx == q.answerIndex) {
                    ButtonDefaults.outlinedButtonColors(
                        disabledContainerColor = Color(0xFF4CAF50),
                        disabledContentColor = Color.White
                    )
                } else if (optionIdx == chosen) {
                    ButtonDefaults.outlinedButtonColors(
                        disabledContainerColor = Color(0xFFE53935),
                        disabledContentColor = Color.White
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            } else {
                ButtonDefaults.outlinedButtonColors()
            }

            OutlinedButton(
                onClick = {
                    if (!answered) {
                        chosen = optionIdx
                        answered = true
                        val isCorrect = optionIdx == q.answerIndex
                        if (isCorrect) score += 1
                        onResult(isCorrect)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !answered,
                colors = btnColors
            ) {
                Text(option)
            }
        }

        item {
            if (answered) {
                val correct = chosen == q.answerIndex
                Text(if (correct) "Correct!" else "Not quite...")
                if (q.reference.isNotBlank()) Text("Reference: ${q.reference}", style = MaterialTheme.typography.bodySmall)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (idx < quizSet.lastIndex) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            idx += 1
                            answered = false
                            chosen = -1
                        }
                    ) {
                        Text("Next Question")
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Quiz Complete!", style = MaterialTheme.typography.titleLarge)
                            Text("Your final score: $score / ${quizSet.size}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                idx = 0
                                score = 0
                                answered = false
                                chosen = -1
                            }) {
                                Text("Restart Quiz")
                            }
                        }
                    }
                }
            }
        }
    }
}
