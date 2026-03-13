package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: SobrietyViewModel) {
    var quitDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var weeklySpend by remember { mutableStateOf("50") }
    var reasons by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Sobriety",
            color = Primary,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            "Welcome to your new beginning.\nLet's set up your journey.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quit Date
                var showDatePicker by remember { mutableStateOf(false) }
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = LocalDate.parse(quitDate).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    quitDate = java.time.Instant.ofEpochMilli(millis)
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

                Column {
                    Text("When did you start your journey?", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = quitDate,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        label = { Text("Start Date") },
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

                // Weekly Spend
                Column {
                    Text("Average weekly spend on alcohol ($)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = weeklySpend,
                        onValueChange = { weeklySpend = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Weekly Spend") }
                    )
                }

                // Reasons
                Column {
                    Text("My reasons for quitting (one per line)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = reasons,
                        onValueChange = { reasons = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        placeholder = { Text("Better health\nSave money\nBe more present") },
                        label = { Text("Reasons") }
                    )
                }

                Button(
                    onClick = {
                        val reasonsList = reasons.split("\n").filter { it.isNotBlank() }
                        viewModel.completeOnboarding(
                            quitDate = quitDate + "T00:00:00",
                            spend = weeklySpend.toIntOrNull() ?: 0,
                            reasons = reasonsList
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("Start My Journey", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
