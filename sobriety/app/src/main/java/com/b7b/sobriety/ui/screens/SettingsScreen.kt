package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.theme.Primary // Assuming Primary is defined here
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    var showAddressDialog by remember { mutableStateOf(false) }

    if (showAddressDialog) {
        AddContactDialog(
            onDismiss = { showAddressDialog = false },
            onConfirm = { name, info ->
                viewModel.addContact(name, info)
                showAddressDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Financial & Journey Section
        SectionTitle(stringResource(R.string.journey_details))
        journeySettingsColumn(uiState, viewModel)

        // Support Resources Section (Moved from separate screen)
        SectionTitle(stringResource(R.string.support_community))
        SupportResourcesList()

        // Coping Toolbox Section (Moved from separate screen)
        SectionTitle(stringResource(R.string.coping_toolbox))
        CopingToolboxList()

        // Emergency Contacts Section
        SectionTitle(stringResource(R.string.emergency_contacts))
        EmergencyContactsColumn(uiState, viewModel) {
            showAddressDialog = true
        }

        // Data Management Section
        SectionTitle(stringResource(R.string.data_management))
        DataManagementColumn(viewModel)

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun journeySettingsColumn(uiState: SobrietyUiState, viewModel: SobrietyViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = uiState.preferences.weeklySpend.toString(),
            onValueChange = {
                val value = it.toIntOrNull() ?: return@OutlinedTextField
                viewModel.updateWeeklySpend(value)
            },
            label = { Text(stringResource(R.string.weekly_spend_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.preferences.personalReasons.joinToString("\n"),
            onValueChange = { viewModel.updateReasons(it.lines().filter { l -> l.isNotBlank() }) },
            label = { Text(stringResource(R.string.reasons_label)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
    }
}

@Composable
fun SupportResourcesList() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            ResourceItem(stringResource(R.string.butabika_desc), "0414 504 388")
            ResourceItem(stringResource(R.string.ushindi_desc), "0702 331 331")
            ResourceItem(stringResource(R.string.aa_central_desc), "0772 411 718")
        }
    }
}

@Composable
fun CopingToolboxList() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.grounding_technique), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.box_breathing), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.call_friend_desc), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.change_env_desc), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ResourceItem(label: String, phone: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(phone, style = MaterialTheme.typography.bodySmall, color = Primary)
        }
    }
}

@Composable
fun EmergencyContactsColumn(uiState: SobrietyUiState, viewModel: SobrietyViewModel, onAddClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
         uiState.preferences.emergencyContacts.forEachIndexed { index, contact ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(contact.name, fontWeight = FontWeight.Medium)
                    Text(contact.info, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { viewModel.removeContact(index) }) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_contact), tint = Color.Red)
                }
            }
        }

        TextButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.add_emergency_contact))
        }
    }
}

@Composable
fun DataManagementColumn(viewModel: SobrietyViewModel) {
    val contextForToast = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                android.widget.Toast.makeText(contextForToast, contextForToast.getString(R.string.exporting_data), android.widget.Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(stringResource(R.string.export_data))
        }

        OutlinedButton(
            onClick = { viewModel.resetData() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        ) {
            Text(stringResource(R.string.reset_all_data))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun AddContactDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_contact)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.name)) })
                TextField(value = info, onValueChange = { info = it }, label = { Text(stringResource(R.string.phone_email)) })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name, info) }) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}
