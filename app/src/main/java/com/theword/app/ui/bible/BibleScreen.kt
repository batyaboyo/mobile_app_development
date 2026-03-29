package com.theword.app.ui.bible

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.selection.SelectionContainer
import com.theword.app.domain.model.*
import com.theword.app.ui.theme.*

@Composable
fun BibleScreen(viewModel: BibleViewModel, onBack: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Version selector
        if (uiState.translations.isNotEmpty()) {
            VersionSelector(
                translations = uiState.translations,
                currentVersion = uiState.currentVersion,
                onVersionChange = { viewModel.changeVersion(it) }
            )
        }

        when (uiState.navState) {
            BibleNavState.BOOKS -> BooksList(uiState.books, uiState.isLoading, onBack) { viewModel.selectBook(it) }
            BibleNavState.CHAPTERS -> ChapterGrid(uiState.selectedBook!!, viewModel)
            BibleNavState.VERSES -> VerseDisplay(uiState, viewModel)
        }
    }
}

@Composable
private fun VersionSelector(
    translations: List<Translation>,
    currentVersion: String,
    onVersionChange: (String) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val current = translations.find { it.id == currentVersion }

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedButton(onClick = { showSheet = true }, modifier = Modifier.fillMaxWidth()) {
            Text("${current?.shortName ?: currentVersion} — ${current?.name ?: ""}")
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(translations.size) { i ->
                    val t = translations[i]
                    val isSelected = t.id == currentVersion
                    ListItem(
                        headlineContent = { Text("${t.shortName} — ${t.name}", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        leadingContent = { if (isSelected) Text("✓", color = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.clickable {
                            onVersionChange(t.id)
                            showSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BooksList(books: List<BibleBook>, isLoading: Boolean, onBack: () -> Unit, onSelect: (BibleBook) -> Unit) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val otBooks = books.filter { it.isOldTestament }
    val ntBooks = books.filter { !it.isOldTestament }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "Select Book",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        item {
            Text(
                "Old Testament",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(otBooks.size) { i ->
            val book = otBooks[i]
            BookItem(book, onSelect)
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Text(
                "New Testament",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(ntBooks.size) { i ->
            val book = ntBooks[i]
            BookItem(book, onSelect)
        }
    }
}

@Composable
private fun BookItem(book: BibleBook, onSelect: (BibleBook) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect(book) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(book.name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Text(
                "${book.chapters} ch.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ChapterGrid(book: BibleBook, viewModel: BibleViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(book.name, style = MaterialTheme.typography.headlineSmall)
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(60.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(book.chapters) { i ->
                val chapter = i + 1
                FilledTonalButton(
                    onClick = { viewModel.selectChapter(chapter) },
                    modifier = Modifier.aspectRatio(1f)
                ) {
                    Text("$chapter")
                }
            }
        }
    }
}

@Composable
private fun VerseDisplay(uiState: BibleUiState, viewModel: BibleViewModel) {
    val context = LocalContext.current
    val book = uiState.selectedBook ?: return
    var showHighlightDialog by remember { mutableStateOf(false) }
    var highlightRef by remember { mutableStateOf("") }
    var highlightText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                "${book.name} ${uiState.selectedChapter}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.previousChapter() },
                enabled = uiState.selectedChapter > 1
            ) {
                Icon(Icons.AutoMirrored.Filled.NavigateBefore, "Previous")
            }
            IconButton(
                onClick = { viewModel.nextChapter() },
                enabled = uiState.selectedChapter < book.chapters
            ) {
                Icon(Icons.AutoMirrored.Filled.NavigateNext, "Next")
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        // Verses
        SelectionContainer {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.chapterContent.size) { index ->
                    when (val item = uiState.chapterContent[index]) {
                        is ChapterContent.Heading -> {
                            Text(
                                item.text,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        is ChapterContent.VerseContent -> {
                            val reference = "${book.name} ${uiState.selectedChapter}:${item.verse.number}"
                            val isBookmarked = reference in uiState.bookmarkedRefs
                            val highlight = uiState.highlightsMap[reference]
                            val bgColor = getHighlightColor(highlight?.color)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(if (bgColor != Color.Transparent) Modifier.background(bgColor) else Modifier)
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)) {
                                            append("${item.verse.number} ")
                                        }
                                        append(item.verse.text)
                                    },
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                // Note indicator
                                if (!highlight?.note.isNullOrBlank()) {
                                    Text(
                                        "📝 ${highlight!!.note}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                // Action row
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    IconButton(onClick = {
                                        highlightRef = reference
                                        highlightText = item.verse.text
                                        showHighlightDialog = true
                                    }, modifier = Modifier.size(32.dp)) {
                                        Text("🎨", fontSize = 14.sp)
                                    }
                                    IconButton(onClick = {
                                        viewModel.toggleFavorite(reference, item.verse.text)
                                    }, modifier = Modifier.size(32.dp)) {
                                        val isFav = reference in uiState.favoriteRefs
                                        Text(if (isFav) "★" else "☆", fontSize = 14.sp)
                                    }
                                    IconButton(onClick = {
                                        viewModel.toggleBookmark(reference, item.verse.text)
                                    }, modifier = Modifier.size(32.dp)) {
                                        Text(if (isBookmarked) "🔖" else "🏳️", fontSize = 14.sp)
                                    }
                                    IconButton(onClick = {
                                        viewModel.toggleBookmark(reference, item.verse.text)
                                    }.takeIf { false } ?: {
                                        viewModel.copyVerse(context, reference, item.verse.text)
                                    }, modifier = Modifier.size(32.dp)) {
                                        Text("📋", fontSize = 14.sp)
                                    }
                                    IconButton(onClick = {
                                        viewModel.shareVerse(context, reference, item.verse.text)
                                    }, modifier = Modifier.size(32.dp)) {
                                        Text("🔗", fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                        is ChapterContent.LineBreak -> Spacer(modifier = Modifier.height(12.dp))
                        is ChapterContent.HebrewSubtitle -> {
                            Text(
                                item.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Highlight dialog
    if (showHighlightDialog) {
        HighlightDialog(
            reference = highlightRef,
            currentColor = uiState.highlightsMap[highlightRef]?.color ?: "none",
            currentNote = uiState.highlightsMap[highlightRef]?.note ?: "",
            onDismiss = { showHighlightDialog = false },
            onSave = { color, note ->
                viewModel.saveHighlight(highlightRef, color, note)
                showHighlightDialog = false
            }
        )
    }
}

@Composable
fun HighlightDialog(
    reference: String,
    currentColor: String,
    currentNote: String,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var selectedColor by remember { mutableStateOf(currentColor) }
    var note by remember { mutableStateOf(currentNote) }

    val colors = listOf("none", "yellow", "green", "blue", "pink", "orange", "purple")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Highlight & Note") },
        text = {
            Column {
                Text(reference, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Highlight Color:", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                    colors.forEach { color ->
                        val bgColor = getHighlightColor(color)
                        val isSelected = selectedColor == color
                        FilledTonalButton(
                            onClick = { selectedColor = color },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (bgColor == Color.Transparent) MaterialTheme.colorScheme.surfaceVariant else bgColor
                            )
                        ) {
                            Text(
                                if (color == "none") "✕" else if (isSelected) "✓" else "",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Personal Note") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedColor, note.ifBlank { null }) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun getHighlightColor(color: String?): Color {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    return when (color) {
        "yellow" -> if (isDark) HighlightYellowDark else HighlightYellow
        "green" -> if (isDark) HighlightGreenDark else HighlightGreen
        "blue" -> if (isDark) HighlightBlueDark else HighlightBlue
        "pink" -> if (isDark) HighlightPinkDark else HighlightPink
        "orange" -> if (isDark) HighlightOrangeDark else HighlightOrange
        "purple" -> if (isDark) HighlightPurpleDark else HighlightPurple
        else -> Color.Transparent
    }
}

private fun Color.luminance(): Float {
    return 0.299f * red + 0.587f * green + 0.114f * blue
}
