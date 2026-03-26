package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.batyaboyo.bibleapp.model.*
import kotlinx.coroutines.launch
import com.batyaboyo.bibleapp.ui.isOldTestament
import com.batyaboyo.bibleapp.ui.getOldTestamentBooks
import com.batyaboyo.bibleapp.ui.getNewTestamentBooks
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleScreen(
    translations: List<Translation>,
    selectedTranslation: Translation?,
    onTranslationSelected: (Translation) -> Unit,
    books: List<Book>,
    selectedBook: Book?,
    onBookSelected: (Book) -> Unit,
    chapterInput: String,
    onChapterChanged: (String) -> Unit,
    verses: List<Verse>,
    status: String?,
    isLoading: Boolean,
    onLoadChapter: () -> Unit,
    onBookmarkVerse: (Verse) -> Unit,
    highlights: List<com.batyaboyo.bibleapp.model.Highlight>,
    onHighlightVerse: (String, String, String) -> Unit,
    commentaries: List<com.batyaboyo.bibleapp.model.Commentary>,
    selectedCommentary: com.batyaboyo.bibleapp.model.Commentary?,
    onCommentarySelected: (com.batyaboyo.bibleapp.model.Commentary?) -> Unit,
    commentaryChapter: com.batyaboyo.bibleapp.model.CommentaryChapter?,
    isCommentaryLoading: Boolean,
    onCompareVerse: (Verse) -> Unit,
    scrollToVerse: Int? = null,
    onScrollComplete: () -> Unit = {}
) {
    var showHighlightDialog by remember { mutableStateOf<Verse?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredVerses = remember(searchQuery, verses) {
        if (searchQuery.isBlank()) verses
        else verses.filter { it.text.contains(searchQuery, ignoreCase = true) || it.reference.contains(searchQuery, ignoreCase = true) }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(scrollToVerse, filteredVerses) {
        val verseToScroll = scrollToVerse
        if (verseToScroll != null && filteredVerses.isNotEmpty()) {
            val verseIndex = filteredVerses.indexOfFirst { it.number == verseToScroll }
            if (verseIndex >= 0) {
                var offset = 0
                if (status != null) offset++
                if (commentaryChapter != null) {
                    offset++
                    offset += commentaryChapter.chapter?.content?.size ?: 0
                } else if (isCommentaryLoading) {
                    offset++
                }
                listState.animateScrollToItem(offset + verseIndex)
                onScrollComplete()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Verses
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search verses...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input"),
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    } else null
                )
            }

            // Status Message
            if (status != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("status_text"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(status, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Commentary Display
            if (commentaryChapter != null) {
                item {
                    Text(
                        "Commentary",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
                items(items = commentaryChapter.chapter?.content ?: emptyList()) { entry ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            if (!entry.number.isNullOrBlank()) {
                                Text(
                                    "Verses ${entry.number}+",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Text(
                                text = entry.content?.joinToString("\n\n") ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else if (isCommentaryLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }

            // Verses Display
            items(items = filteredVerses) { verse ->
                val highlight = highlights.find { it.reference == verse.reference }
                val bgColor = when (highlight?.color) {
                    "yellow" -> Color(0xFFFFF176)
                    "green" -> Color(0xFFAED581)
                    "blue" -> Color(0xFF81D4FA)
                    "pink" -> Color(0xFFF48FB1)
                    "purple" -> Color(0xFFCE93D8)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor)
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            verse.reference,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(verse.text, style = MaterialTheme.typography.bodyLarge)

                        if (!highlight?.note.isNullOrBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    highlight!!.note,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { onCompareVerse(verse) }) { Text("Compare") }
                            TextButton(onClick = { showHighlightDialog = verse }) {
                                Text(if (highlight == null) "Highlight" else "Edit Note")
                            }
                            TextButton(onClick = { onBookmarkVerse(verse) }) { Text("Bookmark") }
                        }
                    }
                }
            }
        }
    }

    if (showHighlightDialog != null) {
        val verse = showHighlightDialog!!
        val existing = highlights.find { it.reference == verse.reference }
        HighlightDialog(
            verse = verse,
            existingHighlight = existing,
            onDismiss = { showHighlightDialog = null },
            onSave = { color, note ->
                onHighlightVerse(verse.reference, color, note)
                showHighlightDialog = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleTopBar(
    selectedBook: Book?,
    chapterInput: String,
    selectedTranslation: Translation?,
    onBookClick: () -> Unit,
    onVersionClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Surface(
                onClick = onBookClick,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedBook?.name ?: "Bible"} $chapterInput",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        actions = {
            Surface(
                onClick = onVersionClick,
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = selectedTranslation?.shortName ?: "VER",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightDialog(
    verse: Verse,
    existingHighlight: com.batyaboyo.bibleapp.model.Highlight?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var selectedColor by remember { mutableStateOf(existingHighlight?.color ?: "none") }
    var note by remember { mutableStateOf(existingHighlight?.note ?: "") }
    
    val colors = listOf(
        "yellow" to Color(0xFFFFF176),
        "green" to Color(0xFFAED581),
        "blue" to Color(0xFF81D4FA),
        "pink" to Color(0xFFF48FB1),
        "purple" to Color(0xFFCE93D8)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Highlight Verse", style = MaterialTheme.typography.headlineSmall)
                Text(text = verse.reference, style = MaterialTheme.typography.titleMedium)
                
                Text(text = "Color", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { (name, color) ->
                        Surface(
                            onClick = { selectedColor = name },
                            color = color,
                            shape = CircleShape,
                            border = if (selectedColor == name) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.size(40.dp)
                        ) {}
                    }
                    Surface(
                        onClick = { selectedColor = "none" },
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        border = if (selectedColor == "none") androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "None", modifier = Modifier.padding(8.dp))
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Add a note") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(selectedColor, note) }) { Text("Save") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleSelectionSheet(
    books: List<Book>,
    selectedBook: Book?,
    onBookSelected: (Book) -> Unit,
    onChapterSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTestament by remember { mutableStateOf(if (selectedBook != null && !isOldTestament(selectedBook.id)) "new" else "old") }
    var showingChapters by remember { mutableStateOf(selectedBook != null) }
    var currentBook by remember { mutableStateOf(selectedBook) }

    Column(modifier = Modifier.fillMaxHeight(0.85f).padding(16.dp)) {
        Text("Select Book & Chapter", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        
        TabRow(selectedTabIndex = if (selectedTestament == "old") 0 else 1) {
            Tab(selected = selectedTestament == "old", onClick = { selectedTestament = "old"; showingChapters = false }) {
                Text("Old Testament", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTestament == "new", onClick = { selectedTestament = "new"; showingChapters = false }) {
                Text("New Testament", modifier = Modifier.padding(12.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        if (!showingChapters) {
            val filteredList: List<Book> = if (selectedTestament == "old") getOldTestamentBooks(books) else getNewTestamentBooks(books)
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                androidx.compose.foundation.lazy.grid.items(filteredList) { bookItem ->
                    Surface(
                        onClick = { 
                            currentBook = bookItem
                            showingChapters = true
                        },
                        color = if (currentBook?.id == bookItem.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            bookItem.name,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        } else {
            val book = currentBook ?: return@Column
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showingChapters = false }) {
                    Icon(imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Back", modifier = Modifier.graphicsLayer(rotationZ = 180f))
                }
                Text(book.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                androidx.compose.foundation.lazy.grid.items((1..book.chapters).toList()) { chapter ->
                    Surface(
                        onClick = { 
                            onBookSelected(book)
                            onChapterSelected(chapter)
                        },
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            chapter.toString(),
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersionSelectionSheet(
    translations: List<Translation>,
    selectedTranslation: Translation?,
    onTranslationSelected: (Translation) -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp)) {
        Text("Select Translation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = translations) { translation ->
                Surface(
                    onClick = { 
                        onTranslationSelected(translation)
                        onDismiss()
                    },
                    color = if (selectedTranslation?.id == translation.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                "${translation.name} (${translation.shortName})",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (selectedTranslation?.id == translation.id) {
                            Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary) 
                        }
                    }
                }
            }
        }
    }
}
