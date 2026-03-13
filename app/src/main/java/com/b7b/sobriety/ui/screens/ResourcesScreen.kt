package com.b7b.sobriety.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.SuccessBg
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Support Resources", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Uganda Support
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Support in Uganda 🇺🇬", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    ResourceLink("Mental Health Uganda", "0800 212 121", "tel:0800212121")
                    ResourceLink("Safe Places Uganda", "+256 782 740 522", "tel:+256782740522")
                    ResourceLink("Kampala Youth Recovery", "+256 774 306 896", "tel:+256774306896")
                }
            }

            // International Support
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Immediate Help (International)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    ResourceLink("US National Helpline", "1-800-662-HELP (4357)", "tel:18006624357")
                    Text(
                        "Free, confidential, 24/7, 365-day-a-year treatment referral.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Communities
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Communities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    ExternalLink("Alcoholics Anonymous (AA)", "https://aa.org")
                    ExternalLink("SMART Recovery", "https://smartrecovery.org")
                    ExternalLink("Reddit r/stopdrinking", "https://www.reddit.com/r/stopdrinking/")
                }
            }

            // Quote Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SuccessBg.copy(alpha = if (uiState.preferences.isDarkTheme) 0.1f else 1f),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, Color(0xFF064E3B).copy(alpha = 0.2f))
            ) {
                Text(
                    "\"You don't have to see the whole staircase, just take the first step.\"",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (uiState.preferences.isDarkTheme) Color.White else Color(0xFF064E3B),
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Data Actions
            val contextForToast = LocalContext.current
            Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { 
                        android.widget.Toast.makeText(contextForToast, "Exporting data to JSON (not yet implemented in this prototype)...", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Export Data")
                }
                
                Button(
                    onClick = { 
                        android.widget.Toast.makeText(contextForToast, "Import logic (waiting for JSON source)...", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Import Data")
                }

                OutlinedButton(
                    onClick = { viewModel.resetData() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset All Data")
                }
            }
        }
    }
}

@Composable
fun ResourceLink(label: String, value: String, uri: String) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(
            value,
            color = Primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(uri)))
            }
        )
    }
}

@Composable
fun ExternalLink(label: String, url: String) {
    val context = LocalContext.current
    Text(
        "• $label",
        color = Primary,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            },
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.bodyMedium
    )
}
