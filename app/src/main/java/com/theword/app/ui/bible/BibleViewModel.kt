package com.theword.app.ui.bible

import android.content.Context
import com.theword.app.ui.util.ShareUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class BibleNavState { BOOKS, CHAPTERS, VERSES }

data class BibleUiState(
    val navState: BibleNavState = BibleNavState.BOOKS,
    val books: List<BibleBook> = emptyList(),
    val translations: List<Translation> = emptyList(),
    val currentVersion: String = "BSB",
    val selectedBook: BibleBook? = null,
    val selectedChapter: Int = 0,
    val chapterContent: List<ChapterContent> = emptyList(),
    val bookmarkedRefs: Set<String> = emptySet(),
    val favoriteRefs: Set<String> = emptySet(),
    val highlightsMap: Map<String, Highlight> = emptyMap(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)

class BibleViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BibleUiState())
    val uiState: StateFlow<BibleUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        observeBookmarks()
        observeHighlights()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val version = repository.prefs.bibleVersion.first()
                val translations = repository.getTranslations()
                val books = repository.getBooks(version)
                _uiState.update {
                    it.copy(
                        translations = translations,
                        books = books,
                        currentVersion = version,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            repository.getAllBookmarks().collect { bookmarks ->
                _uiState.update { it.copy(
                    bookmarkedRefs = bookmarks.map { b -> b.reference }.toSet(),
                    favoriteRefs = bookmarks.filter { b -> b.collection == "Favorites" }.map { b -> b.reference }.toSet()
                ) }
            }
        }
    }

    private fun observeHighlights() {
        viewModelScope.launch {
            repository.getAllHighlights().collect { highlights ->
                _uiState.update { it.copy(highlightsMap = highlights.associateBy { h -> h.reference }) }
            }
        }
    }

    fun selectBook(book: BibleBook) {
        _uiState.update { it.copy(selectedBook = book, navState = BibleNavState.CHAPTERS) }
    }

    fun selectChapter(chapter: Int) {
        val book = _uiState.value.selectedBook ?: return
        _uiState.update { it.copy(selectedChapter = chapter, navState = BibleNavState.VERSES, isLoading = true) }
        viewModelScope.launch {
            try {
                val content = repository.getChapter(_uiState.value.currentVersion, book.id, chapter)
                _uiState.update { it.copy(chapterContent = content, isLoading = false) }
                repository.markChapterRead(book.id, chapter)
                repository.prefs.setLastPosition(book.id, book.name, chapter)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun navigateBack() {
        val current = _uiState.value.navState
        when (current) {
            BibleNavState.VERSES -> _uiState.update { it.copy(navState = BibleNavState.CHAPTERS) }
            BibleNavState.CHAPTERS -> _uiState.update { it.copy(navState = BibleNavState.BOOKS, selectedBook = null) }
            else -> {}
        }
    }
    
    fun selectChapterDeepLink(bookId: String, chapter: Int) {
        viewModelScope.launch {
            // Ensure books are loaded
            if (_uiState.value.books.isEmpty()) {
                val books = repository.getBooks(_uiState.value.currentVersion)
                _uiState.update { it.copy(books = books) }
            }
            
            val book = _uiState.value.books.find { it.id == bookId }
            if (book != null) {
                _uiState.update { it.copy(
                    selectedBook = book,
                    selectedChapter = chapter,
                    navState = BibleNavState.VERSES,
                    isLoading = true
                ) }
                selectChapter(chapter)
            }
        }
    }

    fun previousChapter() {
        val ch = _uiState.value.selectedChapter
        if (ch > 1) selectChapter(ch - 1)
    }

    fun nextChapter() {
        val book = _uiState.value.selectedBook ?: return
        val ch = _uiState.value.selectedChapter
        if (ch < book.chapters) selectChapter(ch + 1)
    }

    fun changeVersion(versionId: String) {
        viewModelScope.launch {
            repository.prefs.setBibleVersion(versionId)
            _uiState.update { it.copy(currentVersion = versionId) }
            loadTranslationData(versionId)
        }
    }

    private suspend fun loadTranslationData(versionId: String) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val books = repository.getBooks(versionId)
            val willReloadChapter = _uiState.value.navState == BibleNavState.VERSES && _uiState.value.selectedBook != null
            
            _uiState.update { 
                it.copy(
                    books = books, 
                    isLoading = if (willReloadChapter) it.isLoading else false 
                ) 
            }
            
            // Reload current chapter if viewing one
            if (willReloadChapter) {
                val selectedBookId = _uiState.value.selectedBook?.id
                val book = if (selectedBookId != null) books.find { it.id == selectedBookId } else null
                if (book != null) {
                    _uiState.update { it.copy(selectedBook = book) }
                    selectChapter(_uiState.value.selectedChapter)
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    fun syncCurrentTranslation() {
        val versionId = _uiState.value.currentVersion
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            try {
                repository.syncTranslation(versionId)
                // Refresh translations to update UI flags
                val translations = repository.getTranslations()
                _uiState.update { it.copy(translations = translations, isSyncing = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSyncing = false, error = e.message) }
            }
        }
    }

    fun toggleBookmark(reference: String, text: String) {
        viewModelScope.launch {
            repository.toggleBookmark(reference, text)
        }
    }

    fun toggleFavorite(reference: String, text: String) {
        viewModelScope.launch {
            repository.toggleFavorite(reference, text)
        }
    }

    fun copyVerse(context: Context, reference: String, text: String) {
        ShareUtils.copyVerse(context, reference, text)
    }

    fun shareVerse(context: Context, reference: String, text: String) {
        ShareUtils.shareVerse(context, reference, text)
    }

    fun saveHighlight(reference: String, color: String, note: String?) {
        viewModelScope.launch {
            repository.saveHighlight(reference, color, note)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BibleViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}
