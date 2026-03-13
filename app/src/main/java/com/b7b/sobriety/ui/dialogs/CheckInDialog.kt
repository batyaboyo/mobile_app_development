package com.b7b.sobriety.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.window.Dialog
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInDialog(
    onDismiss: () -> Unit,
    viewModel: SobrietyViewModel,
    isPastLog: Boolean = false,
    initialDate: String? = null
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedDate by remember { mutableStateOf(initialDate ?: LocalDate.now().toString()) }
    var status by remember { mutableStateOf<String?>(null) }
    var mood by remember { mutableStateOf<String?>(null) }
    var urge by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (step == 1) "Check In" else "Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                if (isPastLog && step == 1) {
                    var showDatePicker by remember { mutableStateOf(false) }
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = LocalDate.parse(selectedDate).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                    )

                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        selectedDate = java.time.Instant.ofEpochMilli(millis)
                                            .atZone(java.time.ZoneId.systemDefault())
                                            .toLocalDate()
                                            .toString()
                                    }
                                    showDatePicker = false
                                }) { Text("OK") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Log Date") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                    contentDescription = "Select Date"
                                )
                            }
                        }
                    )
                }

                when (step) {
                    1 -> {
                        Text("How did ${if (isPastLog) "that day" else "today"} go?")
                        Button(
                            onClick = { status = "sober"; step = 2 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Success)
                        ) {
                            Text("I stayed sober! 🎉")
                        }
                        OutlinedButton(
                            onClick = { status = "slip"; step = 3 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC2410C))
                        ) {
                            Text("I had a slip up")
                        }
                    }

                    2 -> {
                        // Mood & Urge
                        Text("How are you feeling?")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("😊", "🙂", "😐", "😔").forEach { m ->
                                Text(
                                    m,
                                    fontSize = 32.sp,
                                    modifier = Modifier
                                        .clickable { mood = m }
                                        .padding(8.dp)
                                        .alpha(if (mood == m) 1f else 0.4f)
                                )
                            }
                        }

                        Text("Urge Intensity")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("None", "Mild", "Strong", "High").forEach { u ->
                                FilterChip(
                                    selected = urge == u,
                                    onClick = { urge = u },
                                    label = { Text(u) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Journal Note (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                viewModel.checkIn(
                                    CheckIn(
                                        date = selectedDate,
                                        status = status!!,
                                        mood = mood,
                                        urge = urge,
                                        note = note.ifBlank { null }
                                    )
                                )
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save")
                        }
                    }

                    3 -> {
                        // Slip message
                        Text(
                            "It's okay.",
                            style = MaterialTheme.typography.titleLarge,
                            color = Primary
                        )
                        Text(
                            "Setbacks are part of the process. You are still moving forward. This doesn't erase your progress.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Log Slip & Continue")
                        }
                        
                        var showResetConfirm by remember { mutableStateOf(false) }
                        if (!showResetConfirm) {
                            TextButton(onClick = { showResetConfirm = true }) {
                                Text("Start fresh: Set Quit Date to Today", color = Color.Gray, fontSize = 12.sp)
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Are you sure?", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                                TextButton(onClick = { 
                                    viewModel.resetQuitDate()
                                    onDismiss()
                                }) {
                                    Text("Yes, Reset", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                                TextButton(onClick = { showResetConfirm = false }) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
