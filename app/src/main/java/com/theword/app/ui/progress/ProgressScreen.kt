package com.theword.app.ui.progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProgressScreen(viewModel: ProgressViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val overallPct = if (uiState.totalChapters > 0) (uiState.chaptersRead * 100) / uiState.totalChapters else 0
    val otPct = if (929 > 0) (uiState.otChaptersRead * 100) / 929 else 0
    val ntPct = if (260 > 0) (uiState.ntChaptersRead * 100) / 260 else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Reading Progress", style = MaterialTheme.typography.headlineSmall)
        }

        // Overall progress
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Overall Progress", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "$overallPct%",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = overallPct / 100f,
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${uiState.chaptersRead} of ${uiState.totalChapters} chapters read",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // OT / NT breakdown
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ElevatedCard(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Old Testament", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$otPct%", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = otPct / 100f,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            "${uiState.otChaptersRead}/929",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                ElevatedCard(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("New Testament", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$ntPct%", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = ntPct / 100f,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            "${uiState.ntChaptersRead}/260",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Per-book progress
        item {
            Text("Per Book", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        }

        val otBooks = uiState.bookProgressList.filter { it.book.isOldTestament }
        val ntBooks = uiState.bookProgressList.filter { !it.book.isOldTestament }

        if (otBooks.isNotEmpty()) {
            item { Text("Old Testament", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) }
            items(otBooks.size) { i ->
                BookProgressRow(otBooks[i])
            }
        }

        if (ntBooks.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { Text("New Testament", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) }
            items(ntBooks.size) { i ->
                BookProgressRow(ntBooks[i])
            }
        }
    }
}

@Composable
private fun BookProgressRow(bookProgress: BookProgress) {
    val pct = if (bookProgress.book.chapters > 0) bookProgress.chaptersRead.toFloat() / bookProgress.book.chapters else 0f

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            bookProgress.book.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(120.dp)
        )
        LinearProgressIndicator(
            progress = pct,
            modifier = Modifier.weight(1f).height(6.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "${bookProgress.chaptersRead}/${bookProgress.book.chapters}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(40.dp)
        )
    }
}
