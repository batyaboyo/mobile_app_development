package com.theword.app.ui.bookmarks

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.Bookmark
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BookmarksUiState(
    val bookmarks: List<Bookmark> = emptyList(),
    val collections: List<String> = emptyList(),
    val selectedCollection: String? = null
)

class BookmarksViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllBookmarks(),
                repository.prefs.collections
            ) { bookmarks, collStr ->
                val cols = collStr.split(",").filter { it.isNotBlank() }
                BookmarksUiState(bookmarks = bookmarks, collections = cols)
            }.collect { state ->
                _uiState.update { state.copy(selectedCollection = it.selectedCollection) }
            }
        }
    }

    fun selectCollection(collection: String?) {
        _uiState.update { it.copy(selectedCollection = collection) }
    }

    val filteredBookmarks: Flow<List<Bookmark>> = _uiState.map { state ->
        if (state.selectedCollection == null) state.bookmarks
        else state.bookmarks.filter { it.collection == state.selectedCollection }
    }

    fun removeBookmark(reference: String) {
        viewModelScope.launch { repository.removeBookmark(reference) }
    }

    fun moveToCollection(reference: String, collection: String?) {
        viewModelScope.launch { repository.moveBookmarkToCollection(reference, collection) }
    }

    fun copyVerse(context: Context, reference: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("verse", "$text - $reference"))
        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
    }

    fun shareVerse(context: Context, reference: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "\"$text\" - $reference")
        }
        context.startActivity(Intent.createChooser(intent, "Share"))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookmarksViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}
