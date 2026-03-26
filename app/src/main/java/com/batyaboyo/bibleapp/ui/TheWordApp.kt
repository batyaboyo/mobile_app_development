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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.batyaboyo.bibleapp.data.ApiService
import com.batyaboyo.bibleapp.data.AssetBibleProvider
import com.batyaboyo.bibleapp.data.AssetRepository
import com.batyaboyo.bibleapp.data.BibleApi
import com.batyaboyo.bibleapp.data.LocalStore
import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Bookmark
import com.batyaboyo.bibleapp.model.CachedChapter
import com.batyaboyo.bibleapp.model.Commentary
import com.batyaboyo.bibleapp.model.CommentaryChapter
import com.batyaboyo.bibleapp.model.Devotion
import com.batyaboyo.bibleapp.model.Prayer
import com.batyaboyo.bibleapp.model.QuizQuestion
import com.batyaboyo.bibleapp.model.ReadingSession
import com.batyaboyo.bibleapp.model.Story
import com.batyaboyo.bibleapp.model.Translation
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.batyaboyo.bibleapp.model.Verse
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.SheetState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.background



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheWordApp(
    api: BibleApi? = null,
    localStore: LocalStore? = null,
    assetRepo: AssetRepository? = null
) {
    val context = LocalContext.current
    val bibleApi = api ?: remember { AssetBibleProvider(context) }
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
    var dailyMorningPrayer by remember { mutableStateOf<Prayer?>(null) }
    var dailyEveningPrayer by remember { mutableStateOf<Prayer?>(null) }
    var dailyDevotion by remember { mutableStateOf<Devotion?>(null) }
    var dailyStory by remember { mutableStateOf<Story?>(null) }
    var loadingText by remember { mutableStateOf("Loading translations...") }
    var offlineNotice by remember { mutableStateOf<String?>(null) }

    val bookmarks = remember { mutableStateListOf<Bookmark>().apply { addAll(localStoreState.getBookmarks()) } }
    val highlights = remember { mutableStateListOf<com.batyaboyo.bibleapp.model.Highlight>().apply { addAll(localStoreState.getHighlights()) } }
    val bookmarkCollections = remember { mutableStateListOf<String>().apply { addAll(localStoreState.getCollections()) } }

    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var prayers by remember { mutableStateOf<List<Prayer>>(emptyList()) }
    var devotions by remember { mutableStateOf<List<Devotion>>(emptyList()) }
    var commentaries by remember { mutableStateOf<List<Commentary>>(emptyList()) }

    var selectedCommentary by remember { mutableStateOf<Commentary?>(null) }
    var commentaryChapter by remember { mutableStateOf<CommentaryChapter?>(null) }
    var isCommentaryLoading by remember { mutableStateOf(false) }

    var showCompareDialog by remember { mutableStateOf<Verse?>(null) }
    val comparisons = remember { mutableStateMapOf<String, Verse?>() }
    var isComparing by remember { mutableStateOf(false) }
    var scrollToVerse by remember { mutableStateOf<Int?>(null) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var showSelectionSheet by remember { mutableStateOf(false) }
    var showVersionSheet by remember { mutableStateOf(false) }

    val bookSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val versionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun loadChapter() {
        val book = selectedBook ?: return
        val version = selectedTranslation ?: return
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
        devotions = assetRepoState.loadDevotions()
        
        // Calculate daily items
        val dayIndex = (System.currentTimeMillis() / 86_400_000L).toInt()
        if (stories.isNotEmpty()) {
            dailyStory = stories[dayIndex % stories.size]
        }
        if (devotions.isNotEmpty()) {
            dailyDevotion = devotions[dayIndex % devotions.size]
        }
        val morning = prayers.filter { it.type == "morning" }
        if (morning.isNotEmpty()) {
            dailyMorningPrayer = morning[dayIndex % morning.size]
        }
        val evening = prayers.filter { it.type == "evening" }
        if (evening.isNotEmpty()) {
            dailyEveningPrayer = evening[dayIndex % evening.size]
        }

        runCatching { 
            val t = bibleApi.fetchTranslations()
            val c = bibleApi.fetchCommentaries()
            t to c
        }
            .onSuccess { (loadedTranslations, loadedCommentaries) ->
                val localOnly = defaultTranslations().filter { it.id.startsWith("local_") }
                val merged = (localOnly + loadedTranslations).distinctBy { it.id }
                translations = merged
                commentaries = loadedCommentaries
                localStoreState.saveCachedTranslations(merged)
                selectedTranslation = merged.firstOrNull { item -> item.id == restoredReading?.translationId } ?: merged.firstOrNull()
                loadingText = if (merged.isEmpty()) "No translations available." else "Connected"
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

    val bottomItems = listOf(
        TabItem.Home,
        TabItem.Bible,
        TabItem.Bookmarks,
        TabItem.Stories,
        TabItem.Prayer
    )

    Scaffold(
        topBar = {
            if (currentTab == TabItem.Bible) {
                BibleTopBar(
                    selectedBook = selectedBook,
                    chapterInput = chapterInput,
                    selectedTranslation = selectedTranslation,
                    onBookClick = { showSelectionSheet = true },
                    onVersionClick = { showVersionSheet = true }
                )
            } else {
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
                        modifier = Modifier.testTag("tab_${tab.name.lowercase()}"),
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
                    offlineNotice = offlineNotice,
                    morningPrayer = dailyMorningPrayer,
                    eveningPrayer = dailyEveningPrayer,
                    dailyDevotion = dailyDevotion,
                    storyOfDay = dailyStory,
                    onFeatureClick = { currentTab = it },
                    onStoryClick = { selectedStory = it }
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
                    onLoadChapter = { loadChapter() },
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
                        val book = selectedBook
                        val chapter = chapterInput.toIntOrNull()
                        if (book != null && chapter != null) {
                            scope.launch {
                                isCommentaryLoading = true
                                try {
                                    commentaryChapter = if (comm == null) null 
                                        else bibleApi.fetchCommentaryChapter(comm.id, book.id, chapter)
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
                val currentBook = selectedBook
                val currentChapter = chapterInput.toIntOrNull()
                if (currentBook == null || currentChapter == null) {
                    isComparing = false
                    return@LaunchedEffect
                }
                compareIds.filter { it != selectedTranslation?.id }.forEach { versionId ->
                    try {
                        val res = bibleApi.fetchVerse(versionId, currentBook.id, currentChapter, verse.number)
                        comparisons[versionId] = res
                    } catch (e: Exception) {
                        // ignore failures
                    }
                }
                isComparing = false
            }
        }

        if (showSelectionSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSelectionSheet = false },
                sheetState = bookSheetState
            ) {
                BibleSelectionSheet(
                    books = books,
                    selectedBook = selectedBook,
                    onBookSelected = { book ->
                        selectedBook = book
                        chapterInput = "1"
                        verses = emptyList()
                        bibleStatus = null
                    },
                    onChapterSelected = { chap ->
                        chapterInput = chap.toString()
                        loadChapter()
                        scope.launch { bookSheetState.hide() }.invokeOnCompletion {
                            if (!bookSheetState.isVisible) showSelectionSheet = false
                        }
                    },
                    onDismiss = {
                        scope.launch { bookSheetState.hide() }.invokeOnCompletion {
                            if (!bookSheetState.isVisible) showSelectionSheet = false
                        }
                    }
                )
            }
        }

        if (showVersionSheet) {
            ModalBottomSheet(
                onDismissRequest = { showVersionSheet = false },
                sheetState = versionSheetState
            ) {
                VersionSelectionSheet(
                    translations = translations,
                    selectedTranslation = selectedTranslation,
                    onTranslationSelected = { translation ->
                        selectedTranslation = translation
                        verses = emptyList()
                        bibleStatus = null
                        scope.launch { versionSheetState.hide() }.invokeOnCompletion {
                            if (!versionSheetState.isVisible) showVersionSheet = false
                        }
                    },
                    onDismiss = {
                        scope.launch { versionSheetState.hide() }.invokeOnCompletion {
                            if (!versionSheetState.isVisible) showVersionSheet = false
                        }
                    }
                )
            }
        }
    }
}
private fun defaultTranslations(): List<com.batyaboyo.bibleapp.model.Translation> = listOf(
    com.batyaboyo.bibleapp.model.Translation("local_niv", "New International Version (Offline)", "NIV"),
    com.batyaboyo.bibleapp.model.Translation("local_kjv", "King James Version (Offline)", "KJV"),
    com.batyaboyo.bibleapp.model.Translation("BSB", "Berean Standard Bible", "BSB"),
    com.batyaboyo.bibleapp.model.Translation("eng_web", "World English Bible", "WEB"),
    com.batyaboyo.bibleapp.model.Translation("eng_kjv", "King James Version", "KJV"),
    com.batyaboyo.bibleapp.model.Translation("eng_kjva", "King James Version w/ Apocrypha", "KJVA"),
    com.batyaboyo.bibleapp.model.Translation("eng_ylt", "Young's Literal Translation", "YLT")
)

private fun defaultBooks(): List<com.batyaboyo.bibleapp.model.Book> = listOf(
    com.batyaboyo.bibleapp.model.Book("GEN", "Genesis", 50),
    com.batyaboyo.bibleapp.model.Book("EXO", "Exodus", 40),
    com.batyaboyo.bibleapp.model.Book("LEV", "Leviticus", 27),
    com.batyaboyo.bibleapp.model.Book("NUM", "Numbers", 36),
    com.batyaboyo.bibleapp.model.Book("DEU", "Deuteronomy", 34),
    com.batyaboyo.bibleapp.model.Book("JOS", "Joshua", 24),
    com.batyaboyo.bibleapp.model.Book("JDG", "Judges", 21),
    com.batyaboyo.bibleapp.model.Book("RUT", "Ruth", 4),
    com.batyaboyo.bibleapp.model.Book("1SA", "1 Samuel", 31),
    com.batyaboyo.bibleapp.model.Book("2SA", "2 Samuel", 24),
    com.batyaboyo.bibleapp.model.Book("1KI", "1 Kings", 22),
    com.batyaboyo.bibleapp.model.Book("2KI", "2 Kings", 25),
    com.batyaboyo.bibleapp.model.Book("1CH", "1 Chronicles", 29),
    com.batyaboyo.bibleapp.model.Book("2CH", "2 Chronicles", 36),
    com.batyaboyo.bibleapp.model.Book("EZR", "Ezra", 10),
    com.batyaboyo.bibleapp.model.Book("NEH", "Nehemiah", 13),
    com.batyaboyo.bibleapp.model.Book("EST", "Esther", 10),
    com.batyaboyo.bibleapp.model.Book("JOB", "Job", 42),
    com.batyaboyo.bibleapp.model.Book("PSA", "Psalms", 150),
    com.batyaboyo.bibleapp.model.Book("PRO", "Proverbs", 31),
    com.batyaboyo.bibleapp.model.Book("ECC", "Ecclesiastes", 12),
    com.batyaboyo.bibleapp.model.Book("SNG", "Song of Songs", 8),
    com.batyaboyo.bibleapp.model.Book("ISA", "Isaiah", 66),
    com.batyaboyo.bibleapp.model.Book("JER", "Jeremiah", 52),
    com.batyaboyo.bibleapp.model.Book("LAM", "Lamentations", 5),
    com.batyaboyo.bibleapp.model.Book("EZK", "Ezekiel", 48),
    com.batyaboyo.bibleapp.model.Book("DAN", "Daniel", 12),
    com.batyaboyo.bibleapp.model.Book("HOS", "Hosea", 14),
    com.batyaboyo.bibleapp.model.Book("JOL", "Joel", 3),
    com.batyaboyo.bibleapp.model.Book("AMO", "Amos", 9),
    com.batyaboyo.bibleapp.model.Book("OBA", "Obadiah", 1),
    com.batyaboyo.bibleapp.model.Book("JON", "Jonah", 4),
    com.batyaboyo.bibleapp.model.Book("MIC", "Micah", 7),
    com.batyaboyo.bibleapp.model.Book("NAM", "Nahum", 3),
    com.batyaboyo.bibleapp.model.Book("HAB", "Habakkuk", 3),
    com.batyaboyo.bibleapp.model.Book("ZEP", "Zephaniah", 3),
    com.batyaboyo.bibleapp.model.Book("HAG", "Haggai", 2),
    com.batyaboyo.bibleapp.model.Book("ZEC", "Zechariah", 14),
    com.batyaboyo.bibleapp.model.Book("MAL", "Malachi", 4),
    com.batyaboyo.bibleapp.model.Book("MAT", "Matthew", 28),
    com.batyaboyo.bibleapp.model.Book("MRK", "Mark", 16),
    com.batyaboyo.bibleapp.model.Book("LUK", "Luke", 24),
    com.batyaboyo.bibleapp.model.Book("JHN", "John", 21),
    com.batyaboyo.bibleapp.model.Book("ACT", "Acts", 28),
    com.batyaboyo.bibleapp.model.Book("ROM", "Romans", 16),
    com.batyaboyo.bibleapp.model.Book("1CO", "1 Corinthians", 16),
    com.batyaboyo.bibleapp.model.Book("2CO", "2 Corinthians", 13),
    com.batyaboyo.bibleapp.model.Book("GAL", "Galatians", 6),
    com.batyaboyo.bibleapp.model.Book("EPH", "Ephesians", 6),
    com.batyaboyo.bibleapp.model.Book("PHP", "Philippians", 4),
    com.batyaboyo.bibleapp.model.Book("COL", "Colossians", 4),
    com.batyaboyo.bibleapp.model.Book("1TH", "1 Thessalonians", 5),
    com.batyaboyo.bibleapp.model.Book("2TH", "2 Thessalonians", 3),
    com.batyaboyo.bibleapp.model.Book("1TI", "1 Timothy", 6),
    com.batyaboyo.bibleapp.model.Book("2TI", "2 Timothy", 4),
    com.batyaboyo.bibleapp.model.Book("TIT", "Titus", 3),
    com.batyaboyo.bibleapp.model.Book("PHM", "Philemon", 1),
    com.batyaboyo.bibleapp.model.Book("HEB", "Hebrews", 13),
    com.batyaboyo.bibleapp.model.Book("JAS", "James", 5),
    com.batyaboyo.bibleapp.model.Book("1PE", "1 Peter", 5),
    com.batyaboyo.bibleapp.model.Book("2PE", "2 Peter", 3),
    com.batyaboyo.bibleapp.model.Book("1JN", "1 John", 5),
    com.batyaboyo.bibleapp.model.Book("2JN", "2 John", 1),
    com.batyaboyo.bibleapp.model.Book("3JN", "3 John", 1),
    com.batyaboyo.bibleapp.model.Book("JUD", "Jude", 1),
    com.batyaboyo.bibleapp.model.Book("REV", "Revelation", 22)
)

private suspend fun loadDailyVerse(api: com.batyaboyo.bibleapp.data.BibleApi, translationId: String, onReady: (com.batyaboyo.bibleapp.model.Verse?) -> Unit) {
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
