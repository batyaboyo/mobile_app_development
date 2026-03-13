package com.b7b.sobriety.ui.dialogs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.PrimaryLight
import com.b7b.sobriety.viewmodel.SobrietyUiState
import kotlinx.coroutines.delay

@Composable
fun EmergencyDialog(
    onDismiss: () -> Unit,
    uiState: SobrietyUiState
) {
    var timeLeft by remember { mutableIntStateOf(300) } // 5 minutes
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
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
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Emergency Support",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "Breathe. Wait. This feeling will pass.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )

                // Breathing Exercise Integration
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    com.b7b.sobriety.ui.components.BreathingExercise()
                }

                // Emergency Contacts
                if (uiState.preferences.emergencyContacts.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Personal Contacts", fontWeight = FontWeight.Bold)
                        uiState.preferences.emergencyContacts.forEach { contact ->
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${contact.info}")
                                    }
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Call ${contact.name}")
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:18006624357")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Call International Support Line")
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("I'm feeling better")
                }
            }
        }
    }
}
