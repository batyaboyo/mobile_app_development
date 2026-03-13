package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.ui.components.MoodUrgeChart
import com.b7b.sobriety.ui.dialogs.CheckInDialog
import com.b7b.sobriety.ui.dialogs.EmergencyDialog
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    var showCheckIn by remember { mutableStateOf(false) }
    var isPastLog by remember { mutableStateOf(false) }
    var showEmergency by remember { mutableStateOf(false) }

    if (showCheckIn) {
        CheckInDialog(
            onDismiss = { showCheckIn = false },
            viewModel = viewModel,
            isPastLog = isPastLog
        )
    }
    
    if (showEmergency) {
       EmergencyDialog(onDismiss = { showEmergency = false }, uiState = uiState)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sobriety", color = Primary, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(
                            if (uiState.preferences.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Streak Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Current Streak", style = MaterialTheme.typography.titleMedium)
                    Text(
                        uiState.liveTimer,
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = Primary,
                            fontWeight = FontWeight.Black,
                            fontSize = 40.sp
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Text(
                        "Since ${uiState.preferences.quitDate?.split("T")?.get(0) ?: "..."}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Stat Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Saved",
                    value = "$${uiState.moneySaved}",
                    modifier = Modifier.weight(1f),
                    color = Success
                )
                StatCard(
                    label = "Longest (Days)",
                    value = "${uiState.preferences.longestStreak}",
                    modifier = Modifier.weight(1f),
                    color = Success
                )
            }

            // Quote Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Using a simpler card with a border for parity
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(Primary)
                        )
                        Text(
                            "One day at a time.",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontStyle = FontStyle.Italic
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Actions
            val today = java.time.LocalDate.now().toString()
            val hasCheckedInToday = uiState.checkIns.any { it.date == today }

            Button(
                onClick = { 
                    isPastLog = false
                    showCheckIn = true 
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(16.dp),
                enabled = !hasCheckedInToday
            ) {
                Text(if (hasCheckedInToday) "Checked In for Today ✓" else "Check In Today")
            }

            OutlinedButton(
                onClick = { 
                    isPastLog = true
                    showCheckIn = true 
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Log a Past Day")
            }

            Button(
                onClick = { showEmergency = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("🚨 I Need Help Now")
                }
            }

            // Chart
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                MoodUrgeChart(uiState.checkIns)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = color,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
