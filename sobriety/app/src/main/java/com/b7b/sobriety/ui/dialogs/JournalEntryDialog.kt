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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.R
import androidx.compose.ui.window.Dialog
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.util.DateUtils
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryDialog(
    onDismiss: () -> Unit,
    viewModel: SobrietyViewModel,
    initialDate: String? = null
) {
    var selectedDate by remember { mutableStateOf(initialDate ?: LocalDate.now().toString()) }
    var mood by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val parsedSelectedDate = remember(selectedDate) {
        DateUtils.parseDate(selectedDate) ?: LocalDate.now()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = parsedSelectedDate
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
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
                }) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                    stringResource(R.string.new_journal_entry),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Date Picker Field
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.date_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.select_date)
                            )
                        }
                    }
                )

                // Mood Selection
                Text(stringResource(R.string.how_feeling), style = MaterialTheme.typography.titleSmall)
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

                // Journal Text Field
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.journal_note_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                    placeholder = { Text(stringResource(R.string.journal_placeholder)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Save/Cancel Buttons
                Button(
                    onClick = {
                        if (note.isNotBlank()) {
                            viewModel.checkIn(
                                CheckIn(
                                    date = selectedDate,
                                    status = "sober", // Default to sober for journal entries
                                    mood = mood,
                                    note = note
                                )
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = note.isNotBlank()
                ) {
                    Text(stringResource(R.string.save_entry))
                }

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
