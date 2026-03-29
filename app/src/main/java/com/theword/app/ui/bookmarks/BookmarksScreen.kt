package com.theword.app.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.theword.app.domain.model.Bookmark
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel,
    initialCollection: String? = null,
    onNavigateToBible: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredBookmarks by viewModel.filteredBookmarks.collectAsState(initial = emptyList())
    val context = LocalContext.current
    
    // Set initial collection if provided
    LaunchedEffect(initialCollection) {
        if (initialCollection != null) {
            viewModel.selectCollection(initialCollection)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Text(
            "Bookmarks",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        // Collection filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = uiState.selectedCollection == null,
                    onClick = { viewModel.selectCollection(null) },
                    label = { Text("All") }
                )
            }
            items(uiState.collections.size) { i ->
                val collection = uiState.collections[i]
                FilterChip(
                    selected = uiState.selectedCollection == collection,
                    onClick = { viewModel.selectCollection(collection) },
                    label = { Text(collection) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredBookmarks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔖", style = MaterialTheme.typography.displayLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No bookmarks yet", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateToBible) { Text("Start Reading") }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredBookmarks.size) { i ->
                    val bookmark = filteredBookmarks[i]
                    BookmarkCard(
                        bookmark = bookmark,
                        collections = uiState.collections,
                        onCopy = { viewModel.copyVerse(context, bookmark.reference, bookmark.text) },
                        onShare = { viewModel.shareVerse(context, bookmark.reference, bookmark.text) },
                        onRemove = { viewModel.removeBookmark(bookmark.reference) },
                        onMoveToCollection = { viewModel.moveToCollection(bookmark.reference, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    collections: List<String>,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onRemove: () -> Unit,
    onMoveToCollection: (String?) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(bookmark.reference, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                if (bookmark.collection != null) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(bookmark.collection, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "\"${bookmark.text}\"",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.ContentCopy, "Copy", modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Share, "Share", modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.MoreVert, "More", modifier = Modifier.size(16.dp))
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        collections.forEach { collection ->
                            DropdownMenuItem(
                                text = { Text("Move to $collection") },
                                onClick = { showMenu = false; onMoveToCollection(collection) }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Remove from collection") },
                            onClick = { showMenu = false; onMoveToCollection(null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = { showMenu = false; onRemove() }
                        )
                    }
                }
            }
        }
    }
}
