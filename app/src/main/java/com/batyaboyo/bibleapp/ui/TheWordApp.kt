package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.batyaboyo.bibleapp.data.ApiService
import com.batyaboyo.bibleapp.data.AssetRepository
import com.batyaboyo.bibleapp.data.BibleApi
import com.batyaboyo.bibleapp.data.LocalStore
import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Bookmark
import com.batyaboyo.bibleapp.model.CachedChapter
import com.batyaboyo.bibleapp.model.Commentary
import com.batyaboyo.bibleapp.model.CommentaryChapter
import com.batyaboyo.bibleapp.model.Prayer
import com.batyaboyo.bibleapp.model.QuizQuestion
import com.batyaboyo.bibleapp.model.ReadingSession
import com.batyaboyo.bibleapp.model.Story
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse
import kotlinx.coroutines.launch

enum class TabItem(val title: String) {
    Home("Home"),
    Bible("Bible"),
    Bookmarks("Bookmarks"),
    Progress("Progress"),
    Stories("Stories"),
    Prayer("Prayer"),
    Quiz("Quiz"),
    About("About")
}

private fun tabIcon(tab: TabItem): ImageVector = when (tab) {
    TabItem.Home -> Icons.Outlined.Home
    TabItem.Bible -> Icons.AutoMirrored.Outlined.MenuBook
    TabItem.Bookmarks -> Icons.Outlined.BookmarkBorder
    TabItem.Progress -> Icons.Outlined.Timeline
    TabItem.Stories -> Icons.Outlined.AutoStories
    TabItem.Prayer -> Icons.Outlined.SelfImprovement
    TabItem.Quiz -> Icons.Outlined.Psychology
    TabItem.About -> Icons.Outlined.Info
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheWordApp(
    api: BibleApi? = null,
    localStore: LocalStore? = null,
    assetRepo: AssetRepository? = null
) {
    val context = LocalContext.current
    val bibleApi = api ?: remember { ApiService() }
    val localStoreState = localStore ?: remember { LocalStore(context) }
    val assetRepoState = assetRepo ?: remember { AssetRepository(context) }
    val scope = rememberCoroutineScope()
    val restoredReading = remember(localStoreState) { localStoreState.getLastReading() }

    var currentTab by remember { mutableStateOf(TabItem.Home) }

    var translations by remember { mutableStateOf<List<Translation>>(emptyList()) }
    var selectedTranslation by remember { mutableStateOf<Translation?>(null) }
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var chapterInput by remember { mutableStateOf(restoredReading?.chapter?.toString() ?: "1") }
    var verses by remember { mutableStateOf<List<Verse>>(emptyList()) }
    var bibleStatus by remember { mutableStateOf<String?>(null) }
    var isChapterLoading by remember { mutableStateOf(false) }
    var dailyVerse by remember { mutableStateOf<Verse?>(null) }
    var loadingText by remember { mutableStateOf("Loading translations...") }
    var offlineNotice by remember { mutableStateOf<String?>(null) }

    val bookmarks = remember { mutableStateListOf<Bookmark>().apply { addAll(localStoreState.getBookmarks()) } }
    val highlights = remember { mutableStateListOf<com.batyaboyo.bibleapp.model.Highlight>().apply { addAll(localStoreState.getHighlights()) } }
    val bookmarkCollections = remember { mutableStateListOf<String>().apply { addAll(localStoreState.getCollections()) } }

    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var prayers by remember { mutableStateOf<List<Prayer>>(emptyList()) }
    var commentaries by remember { mutableStateOf<List<Commentary>>(emptyList()) }

    var selectedCommentary by remember { mutableStateOf<Commentary?>(null) }
    var commentaryChapter by remember { mutableStateOf<CommentaryChapter?>(null) }
    var isCommentaryLoading by remember { mutableStateOf(false) }

    var showCompareDialog by remember { mutableStateOf<Verse?>(null) }
    val comparisons = remember { mutableStateMapOf<String, Verse?>() }
    var isComparing by remember { mutableStateOf(false) }
    var scrollToVerse by remember { mutableStateOf<Int?>(null) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }

    val loadChapter: () -> Unit = {
        val book = selectedBook ?: return@let
        val version = selectedTranslation ?: return@let
        val chapter = chapterInput.toIntOrNull()?.coerceIn(1, book.chapters) ?: 1
        chapterInput = chapter.toString()
        scope.launch {
            isChapterLoading = true
            runCatching {
                bibleApi.fetchChapter(version.id, book.id, chapter)
            }.onSuccess { loaded ->
                verses = loaded
                localStoreState.saveLastReading(ReadingSession(version.id, book.id, chapter))
                localStoreState.saveCachedChapter(CachedChapter(version.id, book.id, chapter, loaded, System.currentTimeMillis()))
                bibleStatus = if (loaded.isEmpty()) {
                    "No verses found for ${book.name} $chapter (${version.shortName})."
                } else {
                    null
                }
                offlineNotice = null
            }.onFailure {
                val cached = localStoreState.getCachedChapter(version.id, book.id, chapter)
                if (cached != null && cached.verses.isNotEmpty()) {
                    verses = cached.verses
                    bibleStatus = "Offline: showing saved ${book.name} ${cached.chapter} (${version.shortName})."
                    offlineNotice = "No internet. Displaying downloaded chapter."
                } else {
                    verses = emptyList()
                    bibleStatus = "Could not load ${book.name} $chapter (${version.shortName}). Open it once online to save for offline use."
                    offlineNotice = "No internet and this chapter is not cached yet."
                }
            }
            isChapterLoading = false
        }
    }

    LaunchedEffect(Unit) {
        stories = assetRepoState.loadStories()
        questions = assetRepoState.loadQuizQuestions()
        prayers = assetRepoState.loadPrayers()
        runCatching { 
            val t = bibleApi.fetchTranslations()
            val c = bibleApi.fetchCommentaries()
            t to c
        }
            .onSuccess { (loadedTranslations, loadedCommentaries) ->
                translations = loadedTranslations
                commentaries = loadedCommentaries
                localStoreState.saveCachedTranslations(loadedTranslations)
                selectedTranslation = loadedTranslations.firstOrNull { item -> item.id == restoredReading?.translationId } ?: loadedTranslations.firstOrNull()
                loadingText = if (loadedTranslations.isEmpty()) "No translations available." else "Connected"
                offlineNotice = null
            }
            .onFailure {
                val fallback = localStoreState.getCachedTranslations().ifEmpty { defaultTranslations() }
                translations = fallback
                commentaries = emptyList()
                selectedTranslation = fallback.firstOrNull { item -> item.id == restoredReading?.translationId } ?: fallback.firstOrNull()
                loadingText = "Offline mode: using saved translations."
                offlineNotice = "No internet. Showing saved content where available."
            }
    }

    LaunchedEffect(selectedTranslation?.id) {
        val version = selectedTranslation ?: return@LaunchedEffect
        runCatching { bibleApi.fetchBooks(version.id) }
            .onSuccess { loadedBooks ->
                books = loadedBooks
                localStoreState.saveCachedBooks(version.id, loadedBooks)
                val restoredBook = loadedBooks.firstOrNull { book -> book.id == restoredReading?.bookId } ?: loadedBooks.firstOrNull()
                selectedBook = restoredBook
                chapterInput = restoredReading?.takeIf { session -> session.translationId == version.id }?.chapter?.toString() ?: chapterInput
                localStoreState.getCachedChapter(version.id, restoredBook?.id.orEmpty(), chapterInput.toIntOrNull() ?: 1)?.let { cached ->
                    verses = cached.verses
                    bibleStatus = if (cached.verses.isEmpty()) null else "Showing saved chapter ${cached.chapter}."
                } ?: run {
                    verses = emptyList()
                    bibleStatus = null
                }
                loadDailyVerse(bibleApi, version.id) { verse -> dailyVerse = verse }
                offlineNotice = null
            }
            .onFailure {
                val fallbackBooks = localStoreState.getCachedBooks(version.id).ifEmpty { defaultBooks() }
                books = fallbackBooks
                val restoredBook = fallbackBooks.firstOrNull { book -> book.id == restoredReading?.bookId } ?: fallbackBooks.firstOrNull()
                selectedBook = restoredBook
                loadingText = "Offline mode: unable to refresh books for ${version.shortName}."
                bibleStatus = "Using saved/default book list for ${version.shortName}."
                offlineNotice = "You are offline. Load chapters that were previously opened."
            }
    }

    LaunchedEffect(selectedTranslation?.id, selectedBook?.id, chapterInput) {
        val version = selectedTranslation ?: return@LaunchedEffect
        val book = selectedBook ?: return@LaunchedEffect
        val chapter = chapterInput.toIntOrNull()?.takeIf { it > 0 } ?: return@LaunchedEffect
        localStoreState.saveLastReading(ReadingSession(version.id, book.id, chapter))
    }

    LaunchedEffect(selectedCommentary?.id, selectedBook?.id, chapterInput) {
        val commentary = selectedCommentary
        val book = selectedBook
        val chapter = chapterInput.toIntOrNull()
        if (commentary == null || book == null || chapter == null) {
            commentaryChapter = null
            return@LaunchedEffect
        }
        
        isCommentaryLoading = true
        try {
            commentaryChapter = bibleApi.fetchCommentaryChapter(commentary.id, book.id, chapter)
        } catch (e: Exception) {
            // Silent failure for commentary
        } finally {
            isCommentaryLoading = false
        }
    }

    val bottomItems = listOf(TabItem.Home, TabItem.Bible, TabItem.Bookmarks, TabItem.Stories, TabItem.Prayer)
    val topItems = TabItem.entries.filter { it !in bottomItems }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when (currentTab) {
                                TabItem.Home -> "The Word"
                                else -> currentTab.title
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                TabRow(
                    selectedTabIndex = topItems.indexOf(currentTab).let { if (it == -1) 0 else it },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {},
                    indicator = { tabPositions ->
                        if (currentTab in topItems) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[topItems.indexOf(currentTab)])
                            )
                        }
                    }
                ) {
                    topItems.forEach { tab ->
                        Tab(
                            selected = currentTab == tab,
                            onClick = { currentTab = tab },
                            text = { 
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                ) 
                            },
                            icon = { 
                                Icon(
                                    imageVector = tabIcon(tab), 
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(20.dp)
                                ) 
                            },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                bottomItems.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(imageVector = tabIcon(tab), contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        modifier = Modifier.testTag("nav_${tab.name.lowercase()}"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        selectedStory?.let { story ->
            StoryDetailDialog(
                story = story,
                onDismiss = { selectedStory = null },
                onGoToBible = {
                    selectedStory = null
                    val ref = story.keyVerse?.ref ?: ""
                    // Match longest book name first (to handle "1 Samuel" vs "Samuel")
                    val foundBook = books.sortedByDescending { it.name.length }
                        .firstOrNull { ref.startsWith(it.name, ignoreCase = true) }
                    
                    if (foundBook != null) {
                        val afterBook = ref.substring(foundBook.name.length).trim()
                        // Match chapter and optional verse (e.g., "10:25" or "10")
                        val match = Regex("""^(\d+)(?::(\d+))?""").find(afterBook)
                        if (match != null) {
                            val chap = match.groupValues[1]
                            val verse = match.groupValues.getOrNull(2)?.toIntOrNull()
                            
                            selectedBook = foundBook
                            chapterInput = chap
                            scrollToVerse = verse
                            verses = emptyList()
                            bibleStatus = null
                            currentTab = TabItem.Bible
                            loadChapter()
                        }
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                TabItem.Home -> HomeScreen(
                    dailyVerse = dailyVerse,
                    version = selectedTranslation?.shortName ?: "",
                    status = loadingText,
                    bookmarksCount = bookmarks.size,
                    offlineNotice = offlineNotice
                )
                TabItem.Bible -> BibleScreen(
                    translations = translations,
                    selectedTranslation = selectedTranslation,
                    onTranslationSelected = {
                        selectedTranslation = it
                        verses = emptyList()
                        bibleStatus = null
                    },
                    books = books,
                    selectedBook = selectedBook,
                    onBookSelected = {
                        selectedBook = it
                        chapterInput = "1"
                        verses = emptyList()
                        bibleStatus = null
                    },
                    chapterInput = chapterInput,
                    onChapterChanged = { raw -> chapterInput = raw.filter(Char::isDigit).take(3) },
                    verses = verses,
                    status = bibleStatus,
                    isLoading = isChapterLoading,
                    onLoadChapter = loadChapter,
                    onBookmarkVerse = { verse ->
                        val version = selectedTranslation?.shortName ?: ""
                        val bookmark = Bookmark(verse.reference, verse.text, version)
                        localStoreState.addBookmark(bookmark)
                        bookmarks.clear()
                        bookmarks.addAll(localStoreState.getBookmarks())
                    },
                    highlights = highlights,
                    onHighlightVerse = { ref, color, note ->
                        val hl = com.batyaboyo.bibleapp.model.Highlight(ref, color, note)
                        localStoreState.saveHighlight(hl)
                        highlights.clear()
                        highlights.addAll(localStoreState.getHighlights())
                    },
                    commentaries = commentaries,
                    selectedCommentary = selectedCommentary,
                    onCommentarySelected = { comm ->
                        selectedCommentary = comm
                        if (selectedBook != null && chapterInput.toIntOrNull() != null) {
                            scope.launch {
                                isCommentaryLoading = true
                                try {
                                    commentaryChapter = if (comm == null) null 
                                        else bibleApi.fetchCommentaryChapter(comm.id, selectedBook!!.id, chapterInput.toInt())
                                } catch (e: Exception) {
                                    bibleStatus = "Commentary load failed: ${e.message}"
                                } finally {
                                    isCommentaryLoading = false
                                }
                            }
                        }
                    },
                    commentaryChapter = commentaryChapter,
                    isCommentaryLoading = isCommentaryLoading,
                    onCompareVerse = { showCompareDialog = it },
                    scrollToVerse = scrollToVerse,
                    onScrollComplete = { scrollToVerse = null }
                )
                TabItem.Bookmarks -> BookmarksScreen(
                    bookmarks = bookmarks,
                    collections = bookmarkCollections,
                    onRemove = {
                        localStoreState.removeBookmark(it)
                        bookmarks.clear()
                        bookmarks.addAll(localStoreState.getBookmarks())
                    },
                    onAddCollection = { name ->
                        localStoreState.addCollection(name)
                        bookmarkCollections.clear()
                        bookmarkCollections.addAll(localStoreState.getCollections())
                    },
                    onRemoveCollection = { name ->
                        localStoreState.removeCollection(name)
                        bookmarkCollections.clear()
                        bookmarkCollections.addAll(localStoreState.getCollections())
                        bookmarks.clear()
                        bookmarks.addAll(localStoreState.getBookmarks())
                    },
                    onMoveToCollection = { bookmark, coll ->
                        localStoreState.updateBookmarkCollection(bookmark, coll)
                        bookmarks.clear()
                        bookmarks.addAll(localStoreState.getBookmarks())
                    }
                )
                TabItem.Stories -> StoriesScreen(
                    stories = stories,
                    onStoryClick = { story -> selectedStory = story }
                )
                TabItem.Progress -> ProgressScreen(
                    quizStats = localStoreState.getQuizStats(),
                    prayerLog = localStoreState.getPrayerLog()
                )
                TabItem.Prayer -> PrayerScreen(
                    prayers = prayers,
                    onPrayed = { type -> localStoreState.logPrayer(type) }
                )
                TabItem.Quiz -> QuizScreen(
                    questions = questions,
                    onResult = { isCorrect -> localStoreState.saveQuizResult(isCorrect) }
                )
                TabItem.About -> AboutScreen()
            }
        }

        if (showCompareDialog != null) {
            val verse = showCompareDialog!!
            AlertDialog(
                onDismissRequest = { 
                    showCompareDialog = null
                    comparisons.clear()
                },
                title = { Text("Compare: ${selectedBook?.name} ${chapterInput}:${verse.number}") },
                text = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            Text("Current: ${selectedTranslation?.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                            Text(verse.text, style = MaterialTheme.typography.bodyMedium)
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        }
                        if (isComparing) {
                            item { CircularProgressIndicator(Modifier.size(24.dp)) }
                        }
                        val compareIds = listOf("BSB", "eng_web", "eng_bbe")
                        compareIds.filter { it != selectedTranslation?.id }.forEach { versionId ->
                            val v = comparisons[versionId]
                            item {
                                Text(versionId.uppercase(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                                Text(v?.text ?: "Loading...", style = MaterialTheme.typography.bodyMedium)
                                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { 
                        showCompareDialog = null
                        comparisons.clear()
                    }) { Text("Close") }
                }
            )

            LaunchedEffect(verse) {
                isComparing = true
                val compareIds = listOf("BSB", "eng_web", "eng_bbe")
                compareIds.filter { it != selectedTranslation?.id }.forEach { versionId ->
                    try {
                        val res = bibleApi.fetchVerse(versionId, selectedBook!!.id, chapterInput.toInt(), verse.number)
                        comparisons[versionId] = res
                    } catch (e: Exception) {
                        // ignore failures
                    }
                }
                isComparing = false
            }
        }
    }
}

@Composable
private fun ProgressScreen(quizStats: com.batyaboyo.bibleapp.model.QuizStats, prayerLog: Map<String, Map<String, Boolean>>) {
    val prayerStreak = remember(prayerLog) {
        var streak = 0
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val cal = java.util.Calendar.getInstance()
        for (i in 0 until 365) {
            val dateStr = sdf.format(cal.time)
            if (prayerLog.containsKey(dateStr)) {
                streak++
            } else {
                if (i > 0) break // Allow today to not be logged yet
            }
            cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
        }
        streak
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Your Progress", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            HorizontalDivider()
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Prayer Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Current Streak", style = MaterialTheme.typography.labelMedium)
                            Text("$prayerStreak Days", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        Icon(
                            imageVector = Icons.Outlined.SelfImprovement,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Quiz Performance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Accuracy", style = MaterialTheme.typography.labelMedium)
                            val accuracy = if (quizStats.totalQuestions > 0) 
                                (quizStats.correctAnswers * 100 / quizStats.totalQuestions) else 0
                            Text("$accuracy%", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Streak", style = MaterialTheme.typography.labelMedium)
                            Text("${quizStats.streak}", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Best", style = MaterialTheme.typography.labelMedium)
                            Text("${quizStats.bestStreak}", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Total Questions Answered: ${quizStats.totalQuestions}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun PrayerScreen(prayers: List<Prayer>, onPrayed: (String) -> Unit) {
    if (prayers.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Loading prayers...", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    var currentType by remember { mutableStateOf(if (hour < 17) "morning" else "evening") }
    
    val filteredPrayers = prayers.filter { it.type == currentType }
    val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
    
    var selectedPrayer by remember(currentType) { 
        mutableStateOf(filteredPrayers.getOrElse(dayOfYear % filteredPrayers.size) { filteredPrayers.first() }) 
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (currentType == "morning") "🌅 Morning Prayer" else "🌙 Evening Prayer",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = currentType == "morning",
                    onClick = { currentType = "morning" },
                    label = { Text("Morning") }
                )
                FilterChip(
                    selected = currentType == "evening",
                    onClick = { currentType = "evening" },
                    label = { Text("Evening") }
                )
            }
            HorizontalDivider()
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(selectedPrayer.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "\"${selectedPrayer.verse}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            "— ${selectedPrayer.verseRef}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Text(selectedPrayer.text, style = MaterialTheme.typography.bodyMedium)
                    
                    Text(
                        selectedPrayer.closing,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onPrayed(currentType) }
                        ) {
                            Text("I Prayed")
                        }
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedPrayer = filteredPrayers.random()
                            }
                        ) {
                            Text("Another")
                        }
                    }
                }
            }
        }
    }
}



private fun defaultTranslations(): List<Translation> = listOf(
    Translation("BSB", "Berean Standard Bible", "BSB"),
    Translation("eng_web", "World English Bible", "WEB")
)

private fun defaultBooks(): List<Book> = listOf(
    Book("GEN", "Genesis", 50),
    Book("EXO", "Exodus", 40),
    Book("LEV", "Leviticus", 27),
    Book("NUM", "Numbers", 36),
    Book("DEU", "Deuteronomy", 34),
    Book("JOS", "Joshua", 24),
    Book("JDG", "Judges", 21),
    Book("RUT", "Ruth", 4),
    Book("1SA", "1 Samuel", 31),
    Book("2SA", "2 Samuel", 24),
    Book("1KI", "1 Kings", 22),
    Book("2KI", "2 Kings", 25),
    Book("1CH", "1 Chronicles", 29),
    Book("2CH", "2 Chronicles", 36),
    Book("EZR", "Ezra", 10),
    Book("NEH", "Nehemiah", 13),
    Book("EST", "Esther", 10),
    Book("JOB", "Job", 42),
    Book("PSA", "Psalms", 150),
    Book("PRO", "Proverbs", 31),
    Book("ECC", "Ecclesiastes", 12),
    Book("SNG", "Song of Songs", 8),
    Book("ISA", "Isaiah", 66),
    Book("JER", "Jeremiah", 52),
    Book("LAM", "Lamentations", 5),
    Book("EZK", "Ezekiel", 48),
    Book("DAN", "Daniel", 12),
    Book("HOS", "Hosea", 14),
    Book("JOL", "Joel", 3),
    Book("AMO", "Amos", 9),
    Book("OBA", "Obadiah", 1),
    Book("JON", "Jonah", 4),
    Book("MIC", "Micah", 7),
    Book("NAM", "Nahum", 3),
    Book("HAB", "Habakkuk", 3),
    Book("ZEP", "Zephaniah", 3),
    Book("HAG", "Haggai", 2),
    Book("ZEC", "Zechariah", 14),
    Book("MAL", "Malachi", 4),
    Book("MAT", "Matthew", 28),
    Book("MRK", "Mark", 16),
    Book("LUK", "Luke", 24),
    Book("JHN", "John", 21),
    Book("ACT", "Acts", 28),
    Book("ROM", "Romans", 16),
    Book("1CO", "1 Corinthians", 16),
    Book("2CO", "2 Corinthians", 13),
    Book("GAL", "Galatians", 6),
    Book("EPH", "Ephesians", 6),
    Book("PHP", "Philippians", 4),
    Book("COL", "Colossians", 4),
    Book("1TH", "1 Thessalonians", 5),
    Book("2TH", "2 Thessalonians", 3),
    Book("1TI", "1 Timothy", 6),
    Book("2TI", "2 Timothy", 4),
    Book("TIT", "Titus", 3),
    Book("PHM", "Philemon", 1),
    Book("HEB", "Hebrews", 13),
    Book("JAS", "James", 5),
    Book("1PE", "1 Peter", 5),
    Book("2PE", "2 Peter", 3),
    Book("1JN", "1 John", 5),
    Book("2JN", "2 John", 1),
    Book("3JN", "3 John", 1),
    Book("JUD", "Jude", 1),
    Book("REV", "Revelation", 22)
)

private suspend fun loadDailyVerse(api: BibleApi, translationId: String, onReady: (Verse?) -> Unit) {
    val picks = listOf(
        Triple("JHN", 3, 16),
        Triple("PSA", 23, 1),
        Triple("ROM", 8, 28),
        Triple("PHP", 4, 13),
        Triple("JER", 29, 11)
    )
    val pick = picks[(System.currentTimeMillis() / 86_400_000L % picks.size).toInt()]
    runCatching {
        api.fetchChapter(translationId, pick.first, pick.second)
            .firstOrNull { it.number == pick.third }
    }.onSuccess(onReady).onFailure { onReady(null) }
}

@Composable
private fun HomeScreen(
    dailyVerse: Verse?,
    version: String,
    status: String,
    bookmarksCount: Int,
    offlineNotice: String?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("The Word", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Study Scripture with saved progress, bookmarks, stories, and quiz.", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text(
                                text = "Bookmarks: $bookmarksCount",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        if (version.isNotBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.large
                            ) {
                                Text(
                                    text = "Version: $version",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!offlineNotice.isNullOrBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(
                        text = offlineNotice,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            val gradient = Brush.linearGradient(
                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.background(gradient).padding(24.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Verse of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        if (dailyVerse == null) {
                            Text(status, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(
                                "\"${dailyVerse.text}\"",
                                style = MaterialTheme.typography.headlineSmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                "${dailyVerse.reference} ($version)",
                                style = MaterialTheme.typography.labelLarge,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BibleScreen(
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

    var translationExpanded by remember { mutableStateOf(false) }
    var bookExpanded by remember { mutableStateOf(false) }
    var commentaryExpanded by remember { mutableStateOf(false) }
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
                var offset = 6
                if (status != null) offset++
                if (commentaryChapter != null) {
                    offset++ // header
                    offset += commentaryChapter.chapter?.content?.size ?: 0
                } else if (isCommentaryLoading) {
                    offset++
                }
                listState.animateScrollToItem(offset + verseIndex)
                onScrollComplete()
            }
        }
    }

    LazyColumn(state = listState, 
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Read the Bible", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "Choose a version, pick a book and chapter, then load. Chapters you open are saved for offline reading.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
        }

        item {
            ExposedDropdownMenuBox(
                expanded = translationExpanded,
                onExpandedChange = { translationExpanded = !translationExpanded }
            ) {
                OutlinedTextField(
                    value = selectedTranslation?.name ?: "Choose translation",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Version") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = translationExpanded) },
                    modifier = Modifier
                        .testTag("version_selector")
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = translationExpanded,
                    onDismissRequest = { translationExpanded = false }
                ) {
                    translations.take(40).forEach { translation ->
                        DropdownMenuItem(
                            text = { Text("${translation.name} (${translation.shortName})") },
                            onClick = {
                                onTranslationSelected(translation)
                                translationExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            ExposedDropdownMenuBox(
                expanded = bookExpanded,
                onExpandedChange = { bookExpanded = !bookExpanded }
            ) {
                OutlinedTextField(
                    value = selectedBook?.name ?: "Choose book",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Book") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bookExpanded) },
                    modifier = Modifier
                        .testTag("book_selector")
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = bookExpanded,
                    onDismissRequest = { bookExpanded = false }
                ) {
                    books.forEach { book ->
                        DropdownMenuItem(
                            text = { Text(book.name) },
                            onClick = {
                                onBookSelected(book)
                                bookExpanded = false
                            }
                        )
                    }
                }
            }
        }


        item {
            ExposedDropdownMenuBox(
                expanded = commentaryExpanded,
                onExpandedChange = { commentaryExpanded = !commentaryExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCommentary?.name ?: "No Commentary",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Commentary") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = commentaryExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = commentaryExpanded,
                    onDismissRequest = { commentaryExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("No Commentary") },
                        onClick = {
                            onCommentarySelected(null)
                            commentaryExpanded = false
                        }
                    )
                    commentaries.forEach { commentary ->
                        DropdownMenuItem(
                            text = { Text(commentary.name ?: commentary.id) },
                            onClick = {
                                onCommentarySelected(commentary)
                                commentaryExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = chapterInput,
                    onValueChange = onChapterChanged,
                    label = { Text("Chapter") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(120.dp)
                        .testTag("chapter_input")
                )
                Button(
                    onClick = onLoadChapter,
                    enabled = !isLoading,
                    modifier = Modifier
                        .height(56.dp)
                        .testTag("load_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .testTag("loading_indicator"),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Load")
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search verses...") },
                modifier = Modifier.fillMaxWidth().testTag("search_input"),
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

        if (status != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("status_text"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(status, modifier = Modifier.padding(12.dp))
                }
            }
        }


        if (commentaryChapter != null) {
            item {
                Text("Commentary", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                HorizontalDivider()
            }
            items(commentaryChapter.chapter?.content ?: emptyList()) { entry ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(Modifier.padding(12.dp)) {
                        if (!entry.number.isNullOrBlank()) {
                            Text("Verses ${entry.number}+", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
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

        items(filteredVerses) { verse ->
            val highlight = highlights.find { it.reference == verse.reference }
            val bgColor = when (highlight?.color) {
                "yellow" -> androidx.compose.ui.graphics.Color(0xFFFFF176)
                "green" -> androidx.compose.ui.graphics.Color(0xFFAED581)
                "blue" -> androidx.compose.ui.graphics.Color(0xFF81D4FA)
                "pink" -> androidx.compose.ui.graphics.Color(0xFFF48FB1)
                "purple" -> androidx.compose.ui.graphics.Color(0xFFCE93D8)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "${verse.reference}",
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
                        TextButton(onClick = { showHighlightDialog = verse }) { Text(if (highlight == null) "Highlight" else "Edit Note") }
                        TextButton(onClick = { onBookmarkVerse(verse) }) { Text("Bookmark") }
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
private fun HighlightDialog(
    verse: Verse,
    existingHighlight: com.batyaboyo.bibleapp.model.Highlight?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var selectedColor by remember { mutableStateOf(existingHighlight?.color ?: "none") }
    var note by remember { mutableStateOf(existingHighlight?.note ?: "") }
    
    val colors = listOf(
        "yellow" to androidx.compose.ui.graphics.Color(0xFFFFF176),
        "green" to androidx.compose.ui.graphics.Color(0xFFAED581),
        "blue" to androidx.compose.ui.graphics.Color(0xFF81D4FA),
        "pink" to androidx.compose.ui.graphics.Color(0xFFF48FB1),
        "purple" to androidx.compose.ui.graphics.Color(0xFFCE93D8)
    )

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
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
                            shape = androidx.compose.foundation.shape.CircleShape,
                            border = if (selectedColor == name) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.size(40.dp)
                        ) {}
                    }
                    Surface(
                        onClick = { selectedColor = "none" },
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = androidx.compose.foundation.shape.CircleShape,
                        border = if (selectedColor == "none") androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(imageVector = androidx.compose.material.icons.Icons.Default.Close, contentDescription = "None", modifier = Modifier.padding(8.dp))
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
@Composable
private fun BookmarksScreen(
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
                                    Icon(androidx.compose.material.icons.Icons.Default.Close, contentDescription = "Delete", modifier = Modifier.size(12.dp))
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
                            androidx.compose.material3.DropdownMenu(expanded = moveMenuExpanded, onDismissRequest = { moveMenuExpanded = false }) {
                                androidx.compose.material3.DropdownMenuItem(text = { Text("None") }, onClick = { onMoveToCollection(bookmark, null); moveMenuExpanded = false })
                                collections.forEach { coll ->
                                    androidx.compose.material3.DropdownMenuItem(text = { Text(coll) }, onClick = { onMoveToCollection(bookmark, coll); moveMenuExpanded = false })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoriesScreen(stories: List<Story>, onStoryClick: (Story) -> Unit) {
    var filter by remember { mutableStateOf("all") }
    val shown = stories.filter { filter == "all" || it.testament == filter }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Bible Stories", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                FilterChip(
                    selected = filter == "all",
                    onClick = { filter = "all" },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = filter == "old-testament",
                    onClick = { filter = "old-testament" },
                    label = { Text("Old") }
                )
                FilterChip(
                    selected = filter == "new-testament",
                    onClick = { filter = "new-testament" },
                    label = { Text("New") }
                )
            }
            HorizontalDivider()
        }

        items(shown) { story ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStoryClick(story) }
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(story.icon ?: "📖", fontSize = 32.sp, modifier = Modifier.padding(end = 12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                        Text(story.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        story.keyVerse?.ref?.let {
                            Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        val snippet = story.content.firstOrNull()?.text?.take(80)?.plus("...") ?: ""
                        Text(snippet, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryDetailDialog(
    story: Story,
    onDismiss: () -> Unit,
    onGoToBible: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.fillMaxSize()) {
                // Header
                CenterAlignedTopAppBar(
                    title = { Text(story.title, style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(androidx.compose.material.icons.Icons.Default.Close, "Close")
                        }
                    },
                    actions = {
                        story.keyVerse?.let {
                            TextButton(onClick = onGoToBible) {
                                Text("Read in Bible")
                            }
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text(story.icon ?: "📖", fontSize = 64.sp)
                        }
                    }

                    items(story.content) { page ->
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            page.title?.let {
                                Text(it, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Text(page.text, style = MaterialTheme.typography.bodyLarge, lineHeight = 28.sp)
                        }
                    }

                    story.moral?.let {
                        item {
                            Card(
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("The Moral", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text(it, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    story.keyVerse?.let {
                        item {
                            Column(Modifier.padding(vertical = 8.dp)) {
                                Text("Key Verse", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                Text("\"${it.text}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                                Text("- ${it.ref}", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizScreen(questions: List<com.batyaboyo.bibleapp.model.QuizQuestion>, onResult: (Boolean) -> Unit) {
    val quizSet = remember(questions) { questions.shuffled().take(10) }
    var idx by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var chosen by remember { mutableIntStateOf(-1) }

    if (quizSet.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Quiz data missing.")
        }
        return
    }

    val q = quizSet[idx]

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Daily Quiz", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Question ${idx + 1} of ${quizSet.size} | Score: $score")
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }

        item {
            Text(q.category, style = MaterialTheme.typography.labelMedium)
            Text(q.question, style = MaterialTheme.typography.titleMedium)
        }

        items(q.options.indices.toList()) { optionIdx ->
            val option = q.options[optionIdx]
            val btnColors = if (answered) {
                if (optionIdx == q.answerIndex) {
                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        disabledContainerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                        disabledContentColor = androidx.compose.ui.graphics.Color.White
                    )
                } else if (optionIdx == chosen) {
                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        disabledContainerColor = androidx.compose.ui.graphics.Color(0xFFE53935),
                        disabledContentColor = androidx.compose.ui.graphics.Color.White
                    )
                } else {
                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
                }
            } else {
                androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
            }

            OutlinedButton(
                onClick = {
                    if (!answered) {
                        chosen = optionIdx
                        answered = true
                        val isCorrect = optionIdx == q.answerIndex
                        if (isCorrect) score += 1
                        onResult(isCorrect)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !answered,
                colors = btnColors
            ) {
                Text(option)
            }
        }

        item {
            if (answered) {
                val correct = chosen == q.answerIndex
                Text(if (correct) "Correct!" else "Not quite...")
                if (q.reference.isNotBlank()) Text("Reference: ${q.reference}", style = MaterialTheme.typography.bodySmall)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (idx < quizSet.lastIndex) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            idx += 1
                            answered = false
                            chosen = -1
                        }
                    ) {
                        Text("Next Question")
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Quiz Complete!", style = MaterialTheme.typography.titleLarge)
                            Text("Your final score: $score / ${quizSet.size}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                idx = 0
                                score = 0
                                answered = false
                                chosen = -1
                            }) {
                                Text("Restart Quiz")
                            }
                        }
                    }
                }
            }
        }
    }
}
