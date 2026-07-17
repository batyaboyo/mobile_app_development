package com.uganda.learningapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uganda.learningapp.data.AppDatabase
import com.uganda.learningapp.data.entity.BadgeEntity

@Composable
fun ProgressStatsScreen(database: AppDatabase) {
    val completedWeeks by database.roadmapDao().getCompletedWeeksCount().collectAsState(initial = 0)
    val totalWeeks by database.roadmapDao().getTotalWeeksCount().collectAsState(initial = 26)
    val completedTasks by database.roadmapDao().getCompletedTasksCount().collectAsState(initial = 0)
    val totalTasks by database.roadmapDao().getTotalTasksCount().collectAsState(initial = 0)
    val completedProjects by database.roadmapDao().getCompletedProjectsCount().collectAsState(initial = 0)
    val unlockedBadges by database.roadmapDao().getUnlockedBadgesCount().collectAsState(initial = 0)
    val allBadges by database.roadmapDao().getAllBadges().collectAsState(initial = emptyList())

    // Per-phase progress
    val phase1Completed by database.roadmapDao().getCompletedWeeksCountForModule(1).collectAsState(initial = 0)
    val phase1Total by database.roadmapDao().getTotalWeeksCountForModule(1).collectAsState(initial = 6)
    val phase2Completed by database.roadmapDao().getCompletedWeeksCountForModule(2).collectAsState(initial = 0)
    val phase2Total by database.roadmapDao().getTotalWeeksCountForModule(2).collectAsState(initial = 6)
    val phase3Completed by database.roadmapDao().getCompletedWeeksCountForModule(3).collectAsState(initial = 0)
    val phase3Total by database.roadmapDao().getTotalWeeksCountForModule(3).collectAsState(initial = 6)
    val phase4Completed by database.roadmapDao().getCompletedWeeksCountForModule(4).collectAsState(initial = 0)
    val phase4Total by database.roadmapDao().getTotalWeeksCountForModule(4).collectAsState(initial = 8)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📈 Your Progress",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Overall Progress Card
        item {
            ProgressCard(
                title = "Overall Roadmap Progress",
                completedCount = completedWeeks,
                totalCount = totalWeeks.coerceAtLeast(1),
                icon = Icons.Default.Home,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Tasks",
                    value = "$completedTasks/$totalTasks",
                    icon = Icons.Default.Check,
                    color = MaterialTheme.colorScheme.secondary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Projects",
                    value = completedProjects.toString(),
                    icon = Icons.Default.DateRange,
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Badges",
                    value = unlockedBadges.toString(),
                    icon = Icons.Default.Star,
                    color = Color(0xFFFFB800)
                )
            }
        }

        // Phase Progress Section
        item {
            Text(
                text = "Phase Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            PhaseProgressCard(
                title = "Phase 1: Foundations",
                description = "Linux, Networking, Python, Blockchain basics",
                completed = phase1Completed,
                total = phase1Total.coerceAtLeast(1),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            PhaseProgressCard(
                title = "Phase 2: Core Skills",
                description = "Ethical hacking, Smart contracts, Trading",
                completed = phase2Completed,
                total = phase2Total.coerceAtLeast(1),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        item {
            PhaseProgressCard(
                title = "Phase 3: Projects",
                description = "Pentesting labs, DeFi, Portfolio building",
                completed = phase3Completed,
                total = phase3Total.coerceAtLeast(1),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        item {
            PhaseProgressCard(
                title = "Phase 4: Specialization",
                description = "Advanced skills, Career prep, Capstone",
                completed = phase4Completed,
                total = phase4Total.coerceAtLeast(1),
                color = Color(0xFFFF6B6B)
            )
        }

        // Badges Section
        item {
            Text(
                text = "🏆 Badges",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(allBadges) { badge ->
            BadgeCard(badge = badge)
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    completedCount: Int,
    totalCount: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(MaterialTheme.shapes.small),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$completedCount of $totalCount weeks completed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PhaseProgressCard(
    title: String,
    description: String,
    completed: Int,
    total: Int,
    color: Color
) {
    val progress = if (total > 0) completed.toFloat() / total else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "phaseProgress")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$completed/$total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(MaterialTheme.shapes.small),
                color = color,
                trackColor = color.copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
fun BadgeCard(badge: BadgeEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (badge.isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                contentDescription = null,
                tint = if (badge.isUnlocked) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (badge.isUnlocked)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = if (badge.isUnlocked) 1f else 0.5f
                    )
                )
            }
            if (badge.isUnlocked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Unlocked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
