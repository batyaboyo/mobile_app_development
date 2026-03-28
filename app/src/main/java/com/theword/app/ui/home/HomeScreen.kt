package com.theword.app.ui.home

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToBible: (String?, Int?) -> Unit,
    onNavigateToStories: (String?) -> Unit,
    onNavigateToPrayer: (Boolean?, Int?) -> Unit,
    onNavigateToDevotion: () -> Unit,
    onNavigateToProgress: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDate = remember { SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date()) }
    var showContentPopup by remember { mutableStateOf<DailyVerse?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = currentDate,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )

        HeroVerseCard(uiState, onNavigateToBible, onRetry = { viewModel.loadDailyContent() })
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Daily Spiritual Feed", 
            style = MaterialTheme.typography.titleMedium, 
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        // Cards instead of tabs
        if (uiState.dailyDevotion != null) {
            DailyContentCard(
                title = "Daily Devotion",
                subtitle = uiState.dailyDevotion!!.title,
                snippet = uiState.dailyDevotion!!.text,
                icon = Icons.Filled.HistoryEdu,
                onClick = onNavigateToDevotion
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (uiState.dailyStory != null) {
            DailyContentCard(
                title = "Bible Story",
                subtitle = uiState.dailyStory!!.title,
                snippet = uiState.dailyStory!!.snippet,
                icon = Icons.Filled.AutoStories,
                onClick = { onNavigateToStories(uiState.dailyStory!!.id) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (uiState.dailyPrayer != null) {
            val calendar = Calendar.getInstance()
            val isEvening = calendar.get(Calendar.HOUR_OF_DAY) >= 17
            val index = calendar.get(Calendar.DAY_OF_YEAR) // Matches HomeViewModel logic
            
            DailyContentCard(
                title = "Daily Prayer",
                subtitle = uiState.dailyPrayer!!.title,
                snippet = uiState.dailyPrayer!!.text,
                icon = Icons.Filled.SelfImprovement,
                onClick = { onNavigateToPrayer(isEvening, index) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (uiState.dailyPsalm != null) {
            DailyContentCard(
                title = "Daily Psalm",
                subtitle = uiState.dailyPsalm!!.reference,
                snippet = uiState.dailyPsalm!!.text,
                icon = Icons.Filled.MusicNote,
                onClick = { showContentPopup = uiState.dailyPsalm }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (uiState.dailyProverb != null) {
            DailyContentCard(
                title = "Daily Proverb",
                subtitle = uiState.dailyProverb!!.reference,
                snippet = uiState.dailyProverb!!.text,
                icon = Icons.Filled.FormatQuote,
                onClick = { showContentPopup = uiState.dailyProverb }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Added Reading Progress card to use the onNavigateToProgress parameter
        DailyContentCard(
            title = "Personal Growth",
            subtitle = "Reading Progress",
            snippet = "Track your spiritual journey and daily streaks.",
            icon = Icons.Filled.BarChart,
            onClick = onNavigateToProgress
        )
    }

    // Popup for Psalms and Proverbs
    showContentPopup?.let { content ->
        DailyContentDialog(
            content = content,
            onDismiss = { showContentPopup = null },
            onReadFullChapter = {
                showContentPopup = null
                onNavigateToBible(content.bookId, content.chapter) 
            }
        )
    }
}

@Composable
fun DailyContentDialog(
    content: DailyVerse,
    onDismiss: () -> Unit,
    onReadFullChapter: () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = content.reference,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "\"${content.text}\"",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = onReadFullChapter,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Read Full Chapter", fontWeight = FontWeight.Bold)
                    }
                    
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}

@Composable
fun DailyContentCard(
    title: String,
    subtitle: String,
    snippet: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                Text(subtitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    snippet,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun HeroVerseCard(
    uiState: HomeUiState,
    onNavigateToBible: (String?, Int?) -> Unit,
    onRetry: () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Verse of the Day",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    uiState.error != null -> {
                        Text("Unable to load daily verse.", color = MaterialTheme.colorScheme.errorContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary)) {
                            Text("Retry")
                        }
                    }
                    uiState.dailyVerse != null -> {
                        Text(
                            "\"${uiState.dailyVerse.text}\"",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "— ${uiState.dailyVerse.reference}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                FilledTonalButton(
                    onClick = { 
                        onNavigateToBible(uiState.dailyVerse?.bookId, uiState.dailyVerse?.chapter) 
                    }
                ) {
                    Text("Read Full Chapter")
                }
            }
        }
    }
}
