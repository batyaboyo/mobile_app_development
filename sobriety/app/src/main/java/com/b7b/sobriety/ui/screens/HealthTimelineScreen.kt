package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.b7b.sobriety.R
import com.b7b.sobriety.viewmodel.SobrietyUiState

data class HealthItem(val title: String, val description: String)

@Composable
fun HealthTimelineScreen(uiState: SobrietyUiState) {
    val items = listOf(
        HealthItem(stringResource(R.string.health_timeline_1h_title), stringResource(R.string.health_timeline_1h_desc)),
        HealthItem(stringResource(R.string.health_timeline_24h_title), stringResource(R.string.health_timeline_24h_desc)),
        HealthItem(stringResource(R.string.health_timeline_72h_title), stringResource(R.string.health_timeline_72h_desc)),
        HealthItem(stringResource(R.string.health_timeline_1w_title), stringResource(R.string.health_timeline_1w_desc)),
        HealthItem(stringResource(R.string.health_timeline_1m_title), stringResource(R.string.health_timeline_1m_desc)),
        HealthItem(stringResource(R.string.health_timeline_3m_title), stringResource(R.string.health_timeline_3m_desc)),
        HealthItem(stringResource(R.string.health_timeline_1y_title), stringResource(R.string.health_timeline_1y_desc))
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            HealthCard(item)
        }
    }
}

@Composable
fun HealthCard(item: HealthItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
