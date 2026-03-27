package com.theword.app.ui.study

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class StudyFeature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val comingSoon: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    onNavigateToQuiz: () -> Unit,
    onNavigateToBookmarks: () -> Unit
) {
    val context = LocalContext.current
    val comingSoon: () -> Unit = { Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show() }
    val features = listOf(
        StudyFeature("Daily Quiz", "Test your scripture knowledge", Icons.Filled.Quiz, onNavigateToQuiz),
        StudyFeature("Bookmarks", "Access your saved verses", Icons.Filled.Bookmark, onNavigateToBookmarks),
        StudyFeature("Favorites", "Verses you've starred", Icons.Filled.Favorite, comingSoon, comingSoon = true),
        StudyFeature("Comfort", "Verses organized by emotion", Icons.Filled.SelfImprovement, comingSoon, comingSoon = true),
        StudyFeature("Notes", "Personal reflections and diaries", Icons.AutoMirrored.Filled.Notes, comingSoon, comingSoon = true)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Study Hub",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Explore tools and collections to deepen your faith.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(features) { feature ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().height(140.dp).clickable { feature.onClick() },
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Icon(feature.icon, contentDescription = feature.title, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (feature.comingSoon) {
                                Spacer(modifier = Modifier.width(8.dp))
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("Soon", style = MaterialTheme.typography.labelSmall) },
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(feature.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
