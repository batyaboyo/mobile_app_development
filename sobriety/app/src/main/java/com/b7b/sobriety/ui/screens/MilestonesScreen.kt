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
import androidx.compose.ui.res.stringResource
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.viewmodel.SobrietyUiState

@Composable
fun MilestonesScreen(uiState: SobrietyUiState) {
    val milestonesList = listOf(1, 3, 7, 14, 30, 60, 90, 180, 270, 365, 730, 1095) // 1d, 3d, 1w, 2w, 1m... 3y
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(milestonesList) { days ->
            val achieved = uiState.currentStreak >= days
            MilestoneCard(days = days, achieved = achieved, currentStreak = uiState.currentStreak)
        }
    }
}

@Composable
fun MilestoneCard(days: Int, achieved: Boolean, currentStreak: Int) {
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
                    stringResource(R.string.days_count, days),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (achieved) Success else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    if (achieved) stringResource(R.string.unlocked) else stringResource(R.string.upcoming),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!achieved && days > 0) {
                    val progress = (currentStreak.toFloat() / days).coerceIn(0f, 1f)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = Success,
                        trackColor = Success.copy(alpha = 0.1f)
                    )
                }
            }
            Text(
                if (achieved) "🏆" else "🔒",
                fontSize = 32.sp
            )
        }
    }
}
