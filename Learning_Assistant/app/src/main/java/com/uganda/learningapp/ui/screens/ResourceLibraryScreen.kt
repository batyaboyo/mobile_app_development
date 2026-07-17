package com.uganda.learningapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uganda.learningapp.data.AppDatabase
import com.uganda.learningapp.data.entity.ResourceEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceLibraryScreen(database: AppDatabase) {
    val resources by database.roadmapDao().getAllResources().collectAsState(initial = emptyList())
    var selectedPhase by remember { mutableIntStateOf(0) } // 0 = All
    var selectedTopic by remember { mutableStateOf("All") }
    var selectedDifficulty by remember { mutableStateOf("All") }

    val topics = listOf("All", "Linux", "Networking", "Cybersecurity", "Python", "Blockchain", "Trading", "Git", "Career")
    val difficulties = listOf("All", "Beginner", "Intermediate", "Advanced")
    val phases = listOf("All Phases", "Phase 1", "Phase 2", "Phase 3", "Phase 4")

    val filteredResources = resources.filter { resource ->
        (selectedPhase == 0 || resource.phaseId == selectedPhase) &&
        (selectedTopic == "All" || resource.topic == selectedTopic) &&
        (selectedDifficulty == "All" || resource.difficulty == selectedDifficulty)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Text(
            text = "📚 Resource Library",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Phase Filter
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(phases.size) { index ->
                FilterChip(
                    selected = selectedPhase == index,
                    onClick = { selectedPhase = index },
                    label = { Text(phases[index]) },
                    leadingIcon = if (selectedPhase == index) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Topic Filter
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(topics) { topic ->
                FilterChip(
                    selected = selectedTopic == topic,
                    onClick = { selectedTopic = topic },
                    label = { Text(topic) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Difficulty Filter
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(difficulties) { difficulty ->
                FilterChip(
                    selected = selectedDifficulty == difficulty,
                    onClick = { selectedDifficulty = difficulty },
                    label = { Text(difficulty) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (difficulty) {
                            "Beginner" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                            "Intermediate" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                            "Advanced" -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Results count
        Text(
            text = "${filteredResources.size} resources found",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Resource List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredResources) { resource ->
                ResourceCard(resource = resource)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceCard(resource: ResourceEntity) {
    val context = LocalContext.current

    Card(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = when (resource.type) {
                            "Course" -> Icons.Default.Person
                            "YouTube" -> Icons.Default.PlayArrow
                            "GitHub" -> Icons.Default.Share
                            "Lab" -> Icons.Default.Build
                            "Documentation" -> Icons.Default.List
                            else -> Icons.Default.Info
                        },
                        contentDescription = resource.type,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (resource.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = resource.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(resource.type) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = { },
                    label = { Text(resource.topic) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                SuggestionChip(
                    onClick = { },
                    label = { Text(resource.difficulty) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = when (resource.difficulty) {
                            "Beginner" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                            "Intermediate" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                            "Advanced" -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                )
            }
        }
    }
}
