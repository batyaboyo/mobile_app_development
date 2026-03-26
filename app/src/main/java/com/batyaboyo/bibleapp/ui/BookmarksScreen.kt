package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.batyaboyo.bibleapp.model.Bookmark

@Composable
fun BookmarksScreen(
    bookmarks: List<Bookmark>,
    collections: List<String>,
    onRemove: (Bookmark) -> Unit,
    onAddCollection: (String) -> Unit,
    onRemoveCollection: (String) -> Unit,
    onMoveToCollection: (Bookmark, String?) -> Unit
) {
    var newCollName by remember { mutableStateOf("") }
    var selectedCollection by remember { mutableStateOf<String?>(null) }

    val filteredBookmarks = remember(bookmarks, selectedCollection) {
        if (selectedCollection == null) bookmarks
        else bookmarks.filter { it.collection == selectedCollection }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Bookmark Collections", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCollName,
                    onValueChange = { newCollName = it },
                    label = { Text("New Collection") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(onClick = { 
                    if (newCollName.isNotBlank()) {
                        onAddCollection(newCollName)
                        newCollName = ""
                    }
                }) { Text("Add") }
            }
        }

        item {
            ScrollableTabRow(
                selectedTabIndex = if (selectedCollection == null) 0 else collections.indexOf(selectedCollection) + 1,
                edgePadding = 0.dp,
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                divider = {}
            ) {
                Tab(
                    selected = selectedCollection == null,
                    onClick = { selectedCollection = null },
                    text = { Text("All") }
                )
                collections.forEach { coll ->
                    Tab(
                        selected = selectedCollection == coll,
                        onClick = { selectedCollection = coll },
                        text = { 
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(coll)
                                IconButton(onClick = { onRemoveCollection(coll) }, modifier = Modifier.size(16.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Delete", modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    )
                }
            }
        }

        if (filteredBookmarks.isEmpty()) {
            item {
                Text(
                    if (selectedCollection == null) "No bookmarks yet." else "Empty collection.",
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(filteredBookmarks) { bookmark ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(bookmark.reference, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        Text(bookmark.version.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(bookmark.text, style = MaterialTheme.typography.bodyMedium)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var moveMenuExpanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { moveMenuExpanded = true }) {
                                Text("Move to", style = MaterialTheme.typography.labelSmall)
                            }
                            DropdownMenu(expanded = moveMenuExpanded, onDismissRequest = { moveMenuExpanded = false }) {
                                DropdownMenuItem(text = { Text("None") }, onClick = { onMoveToCollection(bookmark, null); moveMenuExpanded = false })
                                collections.forEach { coll ->
                                    DropdownMenuItem(text = { Text(coll) }, onClick = { onMoveToCollection(bookmark, coll); moveMenuExpanded = false })
                                }
                            }
                        }
                        TextButton(onClick = { onRemove(bookmark) }) {
                            Text("Remove", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
