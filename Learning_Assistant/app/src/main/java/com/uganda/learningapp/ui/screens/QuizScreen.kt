package com.uganda.learningapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uganda.learningapp.data.AppDatabase
import com.uganda.learningapp.data.entity.QuizEntity
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    database: AppDatabase,
    weekId: Int,
    onBack: () -> Unit
) {
    val quizzes by database.roadmapDao().getQuizzesForWeek(weekId).collectAsState(initial = emptyList())
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var showFeedback by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Week $weekId Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Exit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (quizzes.isEmpty()) {
                EmptyQuizState()
            } else if (showResult) {
                QuizResultView(
                    score = score,
                    total = quizzes.size,
                    onRetry = {
                        currentQuestionIndex = 0
                        score = 0
                        showResult = false
                        selectedAnswer = -1
                        showFeedback = false
                    },
                    onExit = onBack
                )
            } else {
                val question = quizzes.getOrNull(currentQuestionIndex)
                if (question != null) {
                    // Progress indicator
                    QuizProgressBar(
                        current = currentQuestionIndex + 1,
                        total = quizzes.size
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question card
                    QuestionView(
                        question = question,
                        selectedAnswer = selectedAnswer,
                        showFeedback = showFeedback,
                        onAnswerSelected = { answerIndex ->
                            if (!showFeedback) {
                                selectedAnswer = answerIndex
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Submit/Next button
                    if (!showFeedback) {
                        Button(
                            onClick = {
                                showFeedback = true
                                if (selectedAnswer == question.correctAnswerIndex) {
                                    score++
                                }
                            },
                            enabled = selectedAnswer >= 0,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit Answer")
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentQuestionIndex < quizzes.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = -1
                                    showFeedback = false
                                } else {
                                    showResult = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (currentQuestionIndex < quizzes.size - 1) "Next Question"
                                else "See Results"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizProgressBar(current: Int, total: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question $current of $total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${((current.toFloat() / total) * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = current.toFloat() / total,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun QuestionView(
    question: QuizEntity,
    selectedAnswer: Int,
    showFeedback: Boolean,
    onAnswerSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    val options = listOf(
        question.optionA,
        question.optionB,
        question.optionC,
        question.optionD
    )

    options.forEachIndexed { index, option ->
        AnswerOption(
            text = option,
            index = index,
            isSelected = selectedAnswer == index,
            isCorrect = index == question.correctAnswerIndex,
            showFeedback = showFeedback,
            onClick = { onAnswerSelected(index) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerOption(
    text: String,
    index: Int,
    isSelected: Boolean,
    isCorrect: Boolean,
    showFeedback: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        showFeedback && isCorrect -> Color(0xFF22C55E).copy(alpha = 0.2f)
        showFeedback && isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.2f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        showFeedback && isCorrect -> Color(0xFF22C55E)
        showFeedback && isSelected && !isCorrect -> Color(0xFFEF4444)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(borderColor)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val optionLetter = when (index) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                else -> "D"
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = optionLetter,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (showFeedback) {
                if (isCorrect) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = Color(0xFF22C55E)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Wrong",
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultView(
    score: Int,
    total: Int,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    val percentage = (score.toFloat() / total * 100).toInt()
    val isPassing = percentage >= 70

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isPassing) Icons.Default.Star else Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = if (isPassing) Color(0xFFFFB800) else MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isPassing) "Excellent!" else "Keep Learning!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You scored $score out of $total",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = if (isPassing) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
            Button(onClick = onExit) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Done")
            }
        }
    }
}

@Composable
fun EmptyQuizState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No quizzes available",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Check back later for quiz content",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
