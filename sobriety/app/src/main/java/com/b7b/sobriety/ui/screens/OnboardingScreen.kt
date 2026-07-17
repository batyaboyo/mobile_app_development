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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.util.DateUtils
import com.b7b.sobriety.viewmodel.NavigationEvent
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: SobrietyViewModel, onNavigateToDashboard: () -> Unit) {
    var quitDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var weeklySpend by remember { mutableStateOf("50000") }
    var reasons by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collectLatest { event ->
            if (event is NavigationEvent.OnboardingCompleted) {
                onNavigateToDashboard()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.app_name),
            color = Primary,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            stringResource(R.string.welcome_title),
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
                val parsedQuitDate = remember(quitDate) {
                    DateUtils.parseDate(quitDate) ?: LocalDate.now()
                }
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = parsedQuitDate
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
                                    quitDate = java.time.Instant.ofEpochMilli(millis)
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

                Column {
                    Text(stringResource(R.string.start_journey_q), style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = quitDate,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        label = { Text(stringResource(R.string.start_date_label)) },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = stringResource(R.string.select_date)
                                )
                            }
                        }
                    )
                }

                // Weekly Spend
                Column {
                    Text(stringResource(R.string.weekly_spend_q), style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = weeklySpend,
                        onValueChange = { weeklySpend = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.weekly_spend_label)) }
                    )
                }

                // Reasons
                Column {
                    Text(stringResource(R.string.reasons_q), style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = reasons,
                        onValueChange = { reasons = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        placeholder = { Text(stringResource(R.string.reasons_placeholder)) },
                        label = { Text(stringResource(R.string.reasons_label)) }
                    )
                }

                Button(
                    onClick = {
                        val reasonsList = reasons.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                        val spend = weeklySpend.toIntOrNull()?.coerceAtLeast(0) ?: 50000
                        viewModel.completeOnboarding(
                            quitDate = quitDate + "T00:00:00",
                            spend = spend,
                            reasons = reasonsList
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(stringResource(R.string.start_my_journey), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
