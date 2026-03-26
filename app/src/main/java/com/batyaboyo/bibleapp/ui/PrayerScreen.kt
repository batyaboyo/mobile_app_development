package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.batyaboyo.bibleapp.model.Prayer

@Composable
fun PrayerScreen(
    prayers: List<Prayer>,
    onPrayed: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Daily Prayers", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Find peace and guidance through daily prayer.", style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        items(items = prayers) { prayer ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(prayer.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Icon(
                            imageVector = Icons.Outlined.SelfImprovement,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(prayer.type.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.height(8.dp))
                    Text(prayer.text, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPrayed(prayer.type) }
                    ) {
                        Text("I Prayed This")
                    }
                }
            }
        }
    }
}
