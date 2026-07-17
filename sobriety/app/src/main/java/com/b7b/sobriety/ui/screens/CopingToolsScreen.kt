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
import androidx.compose.ui.res.stringResource
import com.b7b.sobriety.R
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
            TopAppBar(title = { Text(stringResource(R.string.coping_tools), fontWeight = FontWeight.Bold) })
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
                    Text(stringResource(R.string.breathing_exercise_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(R.string.breathing_exercise_desc),
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
                    Text(stringResource(R.string.healthy_distractions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    
                    uiState.preferences.distractions.forEachIndexed { index, distraction ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(distraction, modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.removeDistraction(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove), tint = MaterialTheme.colorScheme.error)
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
                            placeholder = { Text(stringResource(R.string.add_distraction_placeholder)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        IconButton(onClick = {
                            if (newDistraction.isNotBlank()) {
                                viewModel.addDistraction(newDistraction)
                                newDistraction = ""
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                        }
                    }
                }
            }

            // Emergency Contacts
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.emergency_contacts), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove), tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (index < uiState.preferences.emergencyContacts.lastIndex) Divider()
                    }

                    Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newContactName,
                            onValueChange = { newContactName = it },
                            placeholder = { Text(stringResource(R.string.name_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newContactInfo,
                                onValueChange = { newContactInfo = it },
                                placeholder = { Text(stringResource(R.string.info_hint)) },
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
                                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                            }
                        }
                    }
                }
            }
        }
    }
}
