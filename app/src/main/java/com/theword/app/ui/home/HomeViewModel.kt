package com.theword.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.embedded.Devotion
import com.theword.app.data.embedded.DevotionData
import com.theword.app.data.embedded.PopularVerses
import com.theword.app.data.repository.BibleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

data class DailyVerse(val reference: String, val text: String)

data class HomeUiState(
    val dailyVerse: DailyVerse? = null,
    val dailyDevotion: Devotion? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDailyVerse()
    }

    fun loadDailyVerse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val version = repository.prefs.bibleVersion.first()
                val calendar = Calendar.getInstance()
                val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                val verseInfo = PopularVerses.list[dayOfYear % PopularVerses.list.size]
                val devotionInfo = DevotionData.list[dayOfYear % DevotionData.list.size]

                val content = repository.getChapter(version, verseInfo.bookId, verseInfo.chapter)
                val verses = content
                    .filterIsInstance<com.theword.app.domain.model.ChapterContent.VerseContent>()

                val text = if (verseInfo.startVerse != null && verseInfo.endVerse != null) {
                    verses.filter { it.verse.number in verseInfo.startVerse..verseInfo.endVerse }
                        .joinToString(" ") { it.verse.text }
                } else {
                    verses.find { it.verse.number == verseInfo.verse }?.verse?.text ?: ""
                }

                if (text.isNotBlank()) {
                    _uiState.update {
                        it.copy(dailyVerse = DailyVerse(verseInfo.ref, text), dailyDevotion = devotionInfo, isLoading = false)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Verse not found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}
