package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.batyaboyo.bibleapp.model.Story

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesScreen(stories: List<Story>, onStoryClick: (Story) -> Unit) {
    var filter by remember { mutableStateOf("all") }
    val shown = stories.filter { filter == "all" || it.testament == filter }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Bible Stories", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                FilterChip(
                    selected = filter == "all",
                    onClick = { filter = "all" },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = filter == "old-testament",
                    onClick = { filter = "old-testament" },
                    label = { Text("Old") }
                )
                FilterChip(
                    selected = filter == "new-testament",
                    onClick = { filter = "new-testament" },
                    label = { Text("New") }
                )
            }
            HorizontalDivider()
        }

        items(shown) { story ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStoryClick(story) }
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(story.icon ?: "📖", fontSize = 32.sp, modifier = Modifier.padding(end = 12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                        Text(story.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        story.keyVerse?.ref?.let {
                            Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        val snippet = story.content.firstOrNull()?.text?.take(80)?.plus("...") ?: ""
                        Text(snippet, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailDialog(
    story: Story,
    onDismiss: () -> Unit,
    onGoToBible: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.fillMaxSize()) {
                CenterAlignedTopAppBar(
                    title = { Text(story.title, style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, "Close")
                        }
                    },
                    actions = {
                        story.keyVerse?.let {
                            TextButton(onClick = onGoToBible) {
                                Text("Read in Bible")
                            }
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                            Text(story.icon ?: "📖", fontSize = 64.sp)
                        }
                    }

                    items(story.content) { page ->
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            page.title?.let {
                                Text(it, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Text(page.text, style = MaterialTheme.typography.bodyLarge, lineHeight = 28.sp)
                        }
                    }

                    story.moral?.let {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("The Moral", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text(it, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    story.keyVerse?.let {
                        item {
                            Column(Modifier.padding(vertical = 8.dp)) {
                                Text("Key Verse", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                Text("\"${it.text}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                                Text("- ${it.ref}", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}
