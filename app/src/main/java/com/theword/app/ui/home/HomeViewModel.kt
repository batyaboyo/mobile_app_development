package com.theword.app.ui.home

import java.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.embedded.Devotion
import com.theword.app.data.embedded.DevotionData
import com.theword.app.data.embedded.PopularVerses
import com.theword.app.data.embedded.PrayerData
import com.theword.app.data.embedded.STORIES_DATA
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.BibleStory
import com.theword.app.domain.model.Prayer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DailyVerse(
    val reference: String, 
    val text: String,
    val bookId: String? = null,
    val chapter: Int? = null
)

data class HomeUiState(
    val dailyVerse: DailyVerse? = null,
    val dailyDevotion: Devotion? = null,
    val dailyStory: BibleStory? = null,
    val dailyPrayer: Prayer? = null,
    val dailyPsalm: DailyVerse? = null,
    val dailyProverb: DailyVerse? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDailyContent()
    }

    fun loadDailyContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val version = repository.prefs.bibleVersion.first()
                val calendar = Calendar.getInstance()
                val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                
                val verseInfo = PopularVerses.list[dayOfYear % PopularVerses.list.size]
                val devotionInfo = DevotionData.list[dayOfYear % DevotionData.list.size]
                val storyInfo = STORIES_DATA[dayOfYear % STORIES_DATA.size]
                val prayerInfo = PrayerData.morning[dayOfYear % PrayerData.morning.size]

                val content = repository.getChapter(version, verseInfo.bookId, verseInfo.chapter)
                val verses = content
                    .filterIsInstance<com.theword.app.domain.model.ChapterContent.VerseContent>()

                val text = if (verseInfo.startVerse != null && verseInfo.endVerse != null) {
                    verses.filter { it.verse.number in verseInfo.startVerse..verseInfo.endVerse }
                        .joinToString(" ") { it.verse.text }
                } else {
                    verses.find { it.verse.number == verseInfo.verse }?.verse?.text ?: ""
                }

                // Fetch Daily Psalm (PSA)
                val psalmChapter = (dayOfYear % 150) + 1
                val psalmContent = repository.getChapter(version, "PSA", psalmChapter)
                val psalmText = psalmContent
                    .filterIsInstance<com.theword.app.domain.model.ChapterContent.VerseContent>()
                    .take(5) // Get a slightly longer snippet
                    .joinToString(" ") { "${it.verse.number} ${it.verse.text}" }

                // Fetch Daily Proverb (PRO)
                val proverbChapter = (dayOfYear % 31) + 1
                val proverbContent = repository.getChapter(version, "PRO", proverbChapter)
                val proverbText = proverbContent
                    .filterIsInstance<com.theword.app.domain.model.ChapterContent.VerseContent>()
                    .take(3) // Get a slightly longer snippet
                    .joinToString(" ") { "${it.verse.number} ${it.verse.text}" }

                if (text.isNotBlank()) {
                    _uiState.update {
                        it.copy(
                            dailyVerse = DailyVerse(verseInfo.ref, text, verseInfo.bookId, verseInfo.chapter),
                            dailyDevotion = devotionInfo,
                            dailyStory = storyInfo,
                            dailyPrayer = prayerInfo,
                            dailyPsalm = DailyVerse("Psalm $psalmChapter", psalmText, "PSA", psalmChapter),
                            dailyProverb = DailyVerse("Proverbs $proverbChapter", proverbText, "PRO", proverbChapter),
                            isLoading = false
                        )
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
