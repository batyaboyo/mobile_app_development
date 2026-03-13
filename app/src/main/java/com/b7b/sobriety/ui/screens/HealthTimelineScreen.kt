package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.viewmodel.SobrietyUiState

data class HealthBenefit(val days: Int, val title: String, val desc: String)

val healthBenefits = listOf(
    HealthBenefit(1, "Better Hydration", "Your body begins to rehydrate."),
    HealthBenefit(3, "Reduced Anxiety", "Hangover anxiety subsides, sleep may stabilize."),
    HealthBenefit(7, "Better Sleep", "Sleep patterns improve, increasing energy."),
    HealthBenefit(14, "Clearer Skin", "Skin looks brighter, bloating reduces."),
    HealthBenefit(30, "Weight Loss", "Liver fat decreases, mood stabilizes."),
    HealthBenefit(90, "Liver Recovery", "Significant liver healing and cognitive improvement."),
    HealthBenefit(180, "Lower Cancer Risk", "Risk of alcohol-related cancers drops."),
    HealthBenefit(365, "Renewed Health", "Major health risks significantly reduced.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTimelineScreen(uiState: SobrietyUiState) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Health Benefits", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(healthBenefits) { index, benefit ->
                val achieved = uiState.currentStreak >= benefit.days
                TimelineItem(
                    benefit = benefit,
                    achieved = achieved,
                    isLast = index == healthBenefits.lastIndex,
                    currentStreak = uiState.currentStreak
                )
            }
        }
    }
}

@Composable
fun TimelineItem(benefit: HealthBenefit, achieved: Boolean, isLast: Boolean, currentStreak: Int) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        if (achieved) Success else MaterialTheme.colorScheme.outlineVariant,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                "${benefit.title} (${benefit.days} days)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (achieved) Success else MaterialTheme.colorScheme.onSurface
            )
            Text(
                benefit.desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (!achieved) {
                Spacer(Modifier.height(8.dp))
                val progress = (currentStreak.toFloat() / benefit.days).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = Primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    "$currentStreak / ${benefit.days} days",
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
