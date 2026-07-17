package com.theword.app.ui.study

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.JournalEntry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = false,
    val editingEntry: JournalEntry? = null,
    val showEditor: Boolean = false
)

class JournalViewModel(private val repository: BibleRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            repository.getAllJournalEntries().collect { entries ->
                _uiState.update { it.copy(entries = entries, isLoading = false) }
            }
        }
    }

    fun startNewEntry() {
        _uiState.update { it.copy(showEditor = true, editingEntry = null) }
    }

    fun editEntry(entry: JournalEntry) {
        _uiState.update { it.copy(showEditor = true, editingEntry = entry) }
    }

    fun saveEntry(title: String, content: String) {
        viewModelScope.launch {
            val id = _uiState.value.editingEntry?.id ?: 0L
            repository.saveJournalEntry(title, content, id)
            _uiState.update { it.copy(showEditor = false, editingEntry = null) }
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
        }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(showEditor = false, editingEntry = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return JournalViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.showEditor) "Write Reflection" else "Daily Reflections") },
                navigationIcon = {
                    IconButton(onClick = { if (uiState.showEditor) viewModel.cancelEdit() else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!uiState.showEditor) {
                FloatingActionButton(onClick = { viewModel.startNewEntry() }) {
                    Icon(Icons.Default.Add, "New Entry")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.showEditor) {
                JournalEditor(
                    initialTitle = uiState.editingEntry?.title ?: "",
                    initialContent = uiState.editingEntry?.content ?: "",
                    onSave = { t, c -> viewModel.saveEntry(t, c) }
                )
            } else {
                if (uiState.entries.isEmpty()) {
                    EmptyJournalPlaceholder()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.entries) { entry ->
                            JournalEntryCard(
                                entry = entry,
                                dateText = sdf.format(Date(entry.timestamp)),
                                onClick = { viewModel.editEntry(entry) },
                                onDelete = { viewModel.deleteEntry(entry.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntryCard(
    entry: JournalEntry,
    dateText: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun JournalEditor(
    initialTitle: String,
    initialContent: String,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            placeholder = { Text("Today's Reflection...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("What is God speaking to you today?") },
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { if (title.isNotBlank() && content.isNotBlank()) onSave(title, content) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(Icons.Default.Save, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Entry", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun EmptyJournalPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📔", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Your Spiritual Journal",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Write down what you've learned from today's scripture or keep a record of your journey with God.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
