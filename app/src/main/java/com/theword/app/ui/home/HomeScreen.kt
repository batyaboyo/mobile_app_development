package com.theword.app.ui.home

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToBible: () -> Unit,
    onNavigateToStories: () -> Unit,
    onNavigateToPrayer: () -> Unit,
    onNavigateToDevotion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDate = remember { SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp, start = 16.dp, end = 16.dp)
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
            text = "Daily Spiritual Content", 
            style = MaterialTheme.typography.titleMedium, 
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        val tabs = listOf("Story", "Prayer", "Devotion")
        TabRow(
            selectedTabIndex = -1, // No tab is persistently "selected" since they are navigation links
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {}
        ) {
            tabs.forEach { title ->
                Tab(
                    selected = false,
                    onClick = {
                        when(title) {
                            "Story" -> onNavigateToStories()
                            "Prayer" -> onNavigateToPrayer()
                            "Devotion" -> onNavigateToDevotion()
                        }
                    },
                    text = { Text(title, fontWeight = FontWeight.SemiBold) }
                )
            }
        }
    }
}

@Composable
fun HeroVerseCard(
    uiState: HomeUiState,
    onNavigateToBible: () -> Unit,
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
                    onClick = onNavigateToBible,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Read Chapter", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
