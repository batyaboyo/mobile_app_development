package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.ui.dialogs.JournalEntryDialog
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(viewModel: SobrietyViewModel, uiState: SobrietyUiState) {
    var showAddEntry by remember { mutableStateOf(false) }

    if (showAddEntry) {
        JournalEntryDialog(
            onDismiss = { showAddEntry = false },
            viewModel = viewModel
        )
    }

    val journalEntries = uiState.checkIns.filter { it.note != null }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Journal History", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEntry = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Write Entry")
            }
        }
    ) { padding ->
        if (journalEntries.isEmpty()) {
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No entries yet. Check in to add one!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(journalEntries) { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    entry.date,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                entry.mood?.let { Text(it, fontSize = 20.sp) }
                            }
                            entry.urge?.let {
                                Text(
                                    "Urges: $it",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                entry.note!!,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
