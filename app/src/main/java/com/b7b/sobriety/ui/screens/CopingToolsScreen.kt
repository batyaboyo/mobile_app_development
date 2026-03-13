package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.b7b.sobriety.ui.components.BreathingExercise
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopingToolsScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    var newDistraction by remember { mutableStateOf("") }
    var newContactName by remember { mutableStateOf("") }
    var newContactInfo by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Coping Tools", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Breathing Exercise
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("4-7-8 Breathing Exercise", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Inhale for 4 seconds, hold for 7 seconds, exhale for 8 seconds.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    BreathingExercise()
                }
            }

            // Distractions
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Healthy Distractions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    
                    uiState.preferences.distractions.forEachIndexed { index, distraction ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(distraction, modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.removeDistraction(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (index < uiState.preferences.distractions.lastIndex) Divider()
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newDistraction,
                            onValueChange = { newDistraction = it },
                            placeholder = { Text("Add a distraction...") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        IconButton(onClick = {
                            if (newDistraction.isNotBlank()) {
                                viewModel.addDistraction(newDistraction)
                                newDistraction = ""
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            }

            // Emergency Contacts
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Emergency Contacts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    
                    uiState.preferences.emergencyContacts.forEachIndexed { index, contact ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(contact.name, fontWeight = FontWeight.Bold)
                                Text(contact.info, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.removeContact(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (index < uiState.preferences.emergencyContacts.lastIndex) Divider()
                    }

                    Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newContactName,
                            onValueChange = { newContactName = it },
                            placeholder = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newContactInfo,
                                onValueChange = { newContactInfo = it },
                                placeholder = { Text("Phone number or info") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            IconButton(onClick = {
                                if (newContactName.isNotBlank() && newContactInfo.isNotBlank()) {
                                    viewModel.addContact(newContactName, newContactInfo)
                                    newContactName = ""
                                    newContactInfo = ""
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                }
            }
        }
    }
}
