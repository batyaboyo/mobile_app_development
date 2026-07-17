package com.uganda.learningapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uganda.learningapp.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    database: AppDatabase,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val settings by database.roadmapDao().getUserSettings().collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "⚙️ Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Appearance Section
        item {
            SettingsSectionHeader(title = "Appearance")
        }

        item {
            SettingsCard {
                SettingsToggleItem(
                    title = "Dark Mode",
                    description = "Use dark theme for the app",
                    icon = Icons.Default.Settings,
                    checked = isDarkMode,
                    onCheckedChange = { enabled ->
                        onThemeChange(enabled)
                        scope.launch {
                            database.roadmapDao().updateDarkMode(enabled)
                        }
                    }
                )
            }
        }

        // Notifications Section
        item {
            SettingsSectionHeader(title = "Notifications")
        }

        item {
            SettingsCard {
                SettingsToggleItem(
                    title = "Study Reminders",
                    description = "Get daily reminders to study",
                    icon = Icons.Default.Notifications,
                    checked = settings?.notificationsEnabled ?: true,
                    onCheckedChange = { enabled ->
                        scope.launch {
                            database.roadmapDao().updateNotifications(enabled)
                        }
                    }
                )
            }
        }

        // About Section
        item {
            SettingsSectionHeader(title = "About")
        }

        item {
            SettingsCard {
                Column {
                    SettingsInfoItem(
                        title = "App Name",
                        value = "CyberPath Pro",
                        icon = Icons.Default.Info
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsInfoItem(
                        title = "Version",
                        value = "1.0.0",
                        icon = Icons.Default.Build
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsInfoItem(
                        title = "Roadmap Duration",
                        value = "12 Months (52 Weeks)",
                        icon = Icons.Default.DateRange
                    )
                }
            }
        }

        // Learning Path Info
        item {
            SettingsSectionHeader(title = "Learning Path")
        }

        item {
            SettingsCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    LearningPathItem(
                        phase = "Phase 1",
                        title = "Foundations",
                        weeks = "Weeks 1-12",
                        topics = "Linux, Networking, Python, Blockchain basics"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LearningPathItem(
                        phase = "Phase 2",
                        title = "Core Skills",
                        weeks = "Weeks 13-24",
                        topics = "Kali Linux, Ethical Hacking, Smart Contracts, Trading"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LearningPathItem(
                        phase = "Phase 3",
                        title = "Intermediate Projects",
                        weeks = "Weeks 25-36",
                        topics = "Pentesting, DeFi Security, Portfolio Building"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LearningPathItem(
                        phase = "Phase 4",
                        title = "Specialization & Portfolio",
                        weeks = "Weeks 37-52",
                        topics = "Advanced Labs, Career Prep, Capstone Project"
                    )
                }
            }
        }

        // Credits
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Built for ambitious learners worldwide 🌍",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Free resources • Offline-first • 12-month roadmap",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        content()
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsInfoItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LearningPathItem(
    phase: String,
    title: String,
    weeks: String,
    topics: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = phase,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = weeks,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = topics,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
