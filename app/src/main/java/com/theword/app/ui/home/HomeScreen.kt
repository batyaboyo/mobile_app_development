package com.theword.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.theword.app.data.embedded.Devotion
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Feature(val icon: String, val title: String, val description: String)

val features = listOf(
    Feature("📚", "Read the Bible", "All 66 books, organized by Old and New Testament."),
    Feature("🔍", "Search Verses", "Find specific verses or search by keywords."),
    Feature("🔖", "Bookmark & Save", "Save your favorite verses and access them anytime."),
    Feature("📜", "Multiple Versions", "Read in BSB, WEB, BBE, and more translations."),
    Feature("📝", "Commentaries", "Study with Matthew Henry, John Gill, and others."),
    Feature("🌓", "Dark Mode", "Switch between light and dark themes."),
    Feature("❓", "Daily Quiz", "Test your Bible knowledge with a new quiz every day."),
    Feature("🌈", "Bible Stories", "Kid-friendly summaries of important Bible stories."),
    Feature("🎨", "Highlights & Notes", "Highlight verses with colors and add notes."),
    Feature("📊", "Reading Progress", "Track your Bible reading journey."),
    Feature("⚖️", "Compare Translations", "View verses side by side across translations."),
    Feature("📂", "Bookmark Collections", "Organize bookmarks into custom folders."),
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToBible: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero section (span full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            ) {
                Text("Welcome to The Word", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Your personal Bible study companion",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Daily Verse Card (span full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            DailyVerseCard(uiState, onNavigateToBible, onRetry = { viewModel.loadDailyVerse() })
        }

        // Daily Devotion Card (span full width)
        if (uiState.dailyDevotion != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                DailyDevotionCard(uiState.dailyDevotion!!)
            }
        }

        // Feature cards
        items(features) { feature ->
            FeatureCard(feature)
        }
    }
}

@Composable
fun DailyVerseCard(
    uiState: HomeUiState,
    onNavigateToBible: () -> Unit,
    onRetry: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Verse of the Day", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Text("Unable to load daily verse.", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onRetry) { Text("Retry") }
                }
                uiState.dailyVerse != null -> {
                    Text(
                        "\"${uiState.dailyVerse.text}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "— ${uiState.dailyVerse.reference}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToBible) {
                Text("Start Reading")
            }
        }
    }
}

@Composable
fun FeatureCard(feature: Feature) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(feature.icon, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text(feature.title, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                feature.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DailyDevotionCard(devotion: Devotion) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Daily Devotion", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text(devotion.title, style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(devotion.reference, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(devotion.text, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Prayer: ${devotion.prayer}", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
        }
    }
}
