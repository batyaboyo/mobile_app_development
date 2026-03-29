package com.theword.app.ui.stories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theword.app.data.embedded.STORIES_DATA
import com.theword.app.domain.model.BibleStory

@Composable
fun StoriesScreen(initialStoryId: String? = null, onBack: () -> Unit = {}) {
    var selectedStory by remember { 
        mutableStateOf<BibleStory?>(
            if (initialStoryId != null) STORIES_DATA.find { it.id == initialStoryId } else null
        )
    }
    var filter by rememberSaveable { mutableStateOf("All") }

    if (selectedStory != null) {
        StoryDetail(story = selectedStory!!, onBack = { 
            if (initialStoryId != null) onBack() else selectedStory = null 
        })
    } else {
        StoryGrid(filter = filter, onFilterChange = { filter = it }, onSelect = { selectedStory = it }, onBack = onBack)
    }
}

@Composable
private fun StoryGrid(filter: String, onFilterChange: (String) -> Unit, onSelect: (BibleStory) -> Unit, onBack: () -> Unit) {
    val stories = when (filter) {
        "Old Testament" -> STORIES_DATA.filter { it.testament == "old-testament" }
        "New Testament" -> STORIES_DATA.filter { it.testament == "new-testament" }
        else -> STORIES_DATA
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Bible Stories",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Old Testament", "New Testament").forEach { f ->
                FilterChip(
                    selected = filter == f,
                    onClick = { onFilterChange(f) },
                    label = { Text(f) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(stories.size) { i ->
                val story = stories[i]
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().clickable { onSelect(story) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(story.icon, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(story.title, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            story.snippet,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryDetail(story: BibleStory, onBack: () -> Unit) {
    SelectionContainer {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                    Column {
                        Text("${story.icon} ${story.title}", style = MaterialTheme.typography.headlineSmall)
                        Text(story.reference, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            items(story.sections.size) { i ->
                val section = story.sections[i]
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(section.title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(section.text, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 Lesson", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(story.moral, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📖 Key Verse", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("\"${story.keyVerse.text}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                        Text("— ${story.keyVerse.ref}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        }
    }
}
