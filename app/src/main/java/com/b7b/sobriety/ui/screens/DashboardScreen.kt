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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.components.MoodUrgeChart
import com.b7b.sobriety.ui.dialogs.CheckInDialog
import com.b7b.sobriety.ui.dialogs.EmergencyDialog
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.util.DateUtils
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    val savedAmount = NumberFormat.getIntegerInstance(Locale("en", "UG")).format(uiState.moneySaved)

    var showCheckIn by remember { mutableStateOf(false) }
    var isPastLog by remember { mutableStateOf(false) }
    var showEmergency by remember { mutableStateOf(false) }
    var showEmergencyConfirm by remember { mutableStateOf(false) }

    if (showEmergencyConfirm) {
        AlertDialog(
            onDismissRequest = { showEmergencyConfirm = false },
            title = { Text(stringResource(R.string.need_help_q)) },
            text = { Text(stringResource(R.string.emergency_help_desc)) },
            confirmButton = {
                Button(
                    onClick = {
                        showEmergencyConfirm = false
                        showEmergency = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.yes_need_help))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmergencyConfirm = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Text(stringResource(R.string.current_streak), style = MaterialTheme.typography.titleMedium)
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
                    stringResource(R.string.since_date, DateUtils.formatDateForDisplay(uiState.preferences.quitDate)),
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
                label = stringResource(R.string.saved),
                value = stringResource(R.string.money_format, savedAmount),
                modifier = Modifier.weight(1f),
                color = Success
            )
            StatCard(
                label = stringResource(R.string.longest_days),
                value = "${uiState.preferences.longestStreak}",
                modifier = Modifier.weight(1f),
                color = Success
            )
        }

        // Quote Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                        stringResource(R.string.one_day_at_a_time),
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
            Text(if (hasCheckedInToday) stringResource(R.string.checked_in_today_done) else stringResource(R.string.check_in_today))
        }

        OutlinedButton(
            onClick = { 
                isPastLog = true
                showCheckIn = true 
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(stringResource(R.string.log_past_day))
        }

        Button(
            onClick = { showEmergencyConfirm = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.emergency_help))
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
