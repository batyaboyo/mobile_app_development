package com.theword.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.BibleBook
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BookProgress(
    val book: BibleBook,
    val chaptersRead: Int
)

data class ProgressUiState(
    val totalChapters: Int = 1189,
    val chaptersRead: Int = 0,
    val otChaptersRead: Int = 0,
    val ntChaptersRead: Int = 0,
    val bookProgressList: List<BookProgress> = emptyList(),
    val isLoading: Boolean = true
)

class ProgressViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    private fun loadProgress() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val version = repository.prefs.bibleVersion.first()
            val books = repository.getBooks(version)

            repository.getAllProgress().collect { progressList ->
                val readSet = progressList.map { "${it.bookId}_${it.chapter}" }.toSet()
                val totalOT = 929
                val totalNT = 260

                var otRead = 0
                var ntRead = 0
                val bookProgressList = books.map { book ->
                    var count = 0
                    for (ch in 1..book.chapters) {
                        if ("${book.id}_$ch" in readSet) {
                            count++
                            if (book.isOldTestament) otRead++ else ntRead++
                        }
                    }
                    BookProgress(book, count)
                }

                _uiState.update {
                    it.copy(
                        chaptersRead = otRead + ntRead,
                        otChaptersRead = otRead,
                        ntChaptersRead = ntRead,
                        bookProgressList = bookProgressList,
                        isLoading = false
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProgressViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}
