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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.R
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.window.Dialog
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.util.DateUtils
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
                    if (step == 1) stringResource(R.string.check_in_title) else stringResource(R.string.details_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                if (isPastLog && step == 1) {
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

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.log_date_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                    contentDescription = stringResource(R.string.select_date)
                                )
                            }
                        }
                    )
                }

                when (step) {
                    1 -> {
                        Text(stringResource(R.string.how_wait_go, if (isPastLog) stringResource(R.string.that_day) else stringResource(R.string.today)))
                        Button(
                            onClick = { status = "sober"; step = 2 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Success)
                        ) {
                            Text(stringResource(R.string.stayed_sober))
                        }
                        OutlinedButton(
                            onClick = { status = "slip"; step = 3 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(stringResource(R.string.had_slip))
                        }
                    }

                    2 -> {
                        // Mood & Urge
                        Text(stringResource(R.string.how_feeling))
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

                        Text(stringResource(R.string.urge_intensity))
                        val intensities = listOf(
                            stringResource(R.string.intensity_none) to "None",
                            stringResource(R.string.intensity_mild) to "Mild",
                            stringResource(R.string.intensity_strong) to "Strong",
                            stringResource(R.string.intensity_high) to "High"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            intensities.forEach { (label, value) ->
                                FilterChip(
                                    selected = urge == value,
                                    onClick = { urge = value },
                                    label = { Text(label) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text(stringResource(R.string.journal_note_optional)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                val selectedStatus = status ?: return@Button
                                viewModel.checkIn(
                                    CheckIn(
                                        date = selectedDate,
                                        status = selectedStatus,
                                        mood = mood,
                                        urge = urge,
                                        note = note.ifBlank { null }
                                    )
                                )
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.save))
                        }
                    }

                    3 -> {
                        // Slip message
                        Text(
                            stringResource(R.string.it_is_okay),
                            style = MaterialTheme.typography.titleLarge,
                            color = Primary
                        )
                        Text(
                            stringResource(R.string.slip_message),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.log_slip_continue))
                        }
                        
                        var showResetConfirm by remember { mutableStateOf(false) }
                        if (!showResetConfirm) {
                            TextButton(onClick = { showResetConfirm = true }) {
                                Text(stringResource(R.string.start_fresh_reset), color = Color.Gray, fontSize = 12.sp)
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(stringResource(R.string.reset_confirm_q), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                                TextButton(onClick = { 
                                    viewModel.resetQuitDate()
                                    onDismiss()
                                }) {
                                    Text(stringResource(R.string.yes_reset), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                                TextButton(onClick = { showResetConfirm = false }) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        }
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
