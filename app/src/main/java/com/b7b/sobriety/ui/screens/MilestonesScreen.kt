package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.viewmodel.SobrietyUiState

val milestonesList = listOf(7, 30, 60, 90, 180, 365, 730)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestonesScreen(uiState: SobrietyUiState) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Milestones & Rewards", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(milestonesList) { days ->
                val achieved = uiState.currentStreak >= days
                MilestoneCard(days = days, achieved = achieved)
            }
        }
    }
}

@Composable
fun MilestoneCard(days: Int, achieved: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = if (achieved) BorderStroke(1.dp, Success) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (achieved) Success.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "$days Days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (achieved) Success else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    if (achieved) "Unlocked!" else "Upcoming",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                if (achieved) "🏆" else "🔒",
                fontSize = 32.sp
            )
        }
    }
}
