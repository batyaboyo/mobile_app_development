package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.b7b.sobriety.R
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.ui.dialogs.JournalEntryDialog
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel

@Composable
fun JournalScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<CheckIn?>(null) }

    if (showDialog) {
        JournalEntryDialog(
            onDismiss = {
                showDialog = false
                selectedEntry = null
            },
            viewModel = viewModel,
            initialDate = selectedEntry?.date
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.checkIns.none { it.note?.isNotEmpty() == true }) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.no_journal_entries),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    stringResource(R.string.journal_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.checkIns.filter { it.note?.isNotEmpty() == true }.sortedByDescending { it.date }) { entry ->
                    JournalEntryCard(entry) {
                        selectedEntry = entry
                        showDialog = true
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Primary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_entry_desc))
        }
    }
}

@Composable
fun JournalEntryCard(entry: CheckIn, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
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
            if (entry.urge != null) {
                Text(
                    stringResource(R.string.urge_level, entry.urge),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                entry.note ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
