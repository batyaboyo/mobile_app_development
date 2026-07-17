package com.theword.app.ui.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Daily Bible Quiz", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            
            // Stats Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔥", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${uiState.streak}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${uiState.totalPoints}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.alreadyTaken -> AlreadyTakenView(uiState)
            uiState.isComplete -> ResultsView(viewModel, uiState)
            uiState.questions.isNotEmpty() -> QuestionView(viewModel, uiState)
            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun AlreadyTakenView(uiState: QuizUiState) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ElevatedCard {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✅", style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Quiz Already Completed!", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Score: ${uiState.previousScore}/${uiState.previousTotal}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Come back tomorrow for a new quiz!", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun QuestionView(viewModel: QuizViewModel, uiState: QuizUiState) {
    val question = uiState.questions[uiState.currentIndex]
    val answered = uiState.answers[uiState.currentIndex]

    Column {
        // Progress bar
        LinearProgressIndicator(
            progress = { (uiState.currentIndex + 1).toFloat() / uiState.questions.size },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Question ${uiState.currentIndex + 1} of ${uiState.questions.size}",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        SuggestionChip(onClick = {}, label = { Text(question.category) })
        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(question.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                question.options.forEachIndexed { index, option ->
                    val bgColor by animateColorAsState(
                        targetValue = when {
                            answered != null && index == question.answerIndex -> MaterialTheme.colorScheme.primaryContainer
                            answered != null && index == answered.selectedIndex && !answered.correct -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "option_bg"
                    )

                    OutlinedButton(
                        onClick = { if (answered == null) viewModel.submitAnswer(index) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = bgColor),
                        enabled = answered == null
                    ) {
                        Text(option, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                if (answered != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (answered.correct) "✅ Correct!" else "❌ Incorrect",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (answered.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Text(
                        "Reference: ${question.reference}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultsView(viewModel: QuizViewModel, uiState: QuizUiState) {
    val score = viewModel.score
    val total = uiState.questions.size
    val pct = (score * 100) / total

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        when {
                            pct >= 90 -> "🏆"
                            pct >= 70 -> "⭐"
                            pct >= 50 -> "👍"
                            else -> "📖"
                        },
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$score/$total", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                    Text(
                        when {
                            pct >= 90 -> "Outstanding!"
                            pct >= 70 -> "Great Job!"
                            pct >= 50 -> "Good Effort!"
                            else -> "Keep Studying!"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        item {
            TextButton(onClick = { viewModel.toggleReview() }) {
                Text(if (uiState.showReview) "Hide Review" else "Review Answers")
            }
        }

        if (uiState.showReview) {
            items(uiState.questions.size) { i ->
                val question = uiState.questions[i]
                val answer = uiState.answers[i]
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "${if (answer?.correct == true) "✅" else "❌"} ${question.question}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Your answer: ${question.options.getOrElse(answer?.selectedIndex ?: -1) { "—" }}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Correct: ${question.options[question.answerIndex]}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            question.reference,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
