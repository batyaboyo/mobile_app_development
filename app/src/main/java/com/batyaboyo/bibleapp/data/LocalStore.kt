package com.batyaboyo.bibleapp.data

import android.content.Context
import android.util.Log
import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Bookmark
import com.batyaboyo.bibleapp.model.CachedChapter
import com.batyaboyo.bibleapp.model.Highlight
import com.batyaboyo.bibleapp.model.Prayer
import com.batyaboyo.bibleapp.model.QuizStats
import com.batyaboyo.bibleapp.model.ReadingSession
import com.batyaboyo.bibleapp.model.Translation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalStore(context: Context, prefsName: String = "the_word_store") {
    private val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun getBookmarks(): List<Bookmark> {
        return read<List<Bookmark>>(KEY_BOOKMARKS).orEmpty()
    }

    fun addBookmark(bookmark: Bookmark) {
        val current = getBookmarks().toMutableList()
        if (current.any { it.reference == bookmark.reference && it.version == bookmark.version }) return
        current.add(0, bookmark)
        saveBookmarks(current)
    }

    fun removeBookmark(bookmark: Bookmark) {
        val filtered = getBookmarks().filterNot {
            it.reference == bookmark.reference && it.version == bookmark.version
        }
        saveBookmarks(filtered)
    }

    fun getLastReading(): ReadingSession? = read(KEY_LAST_READING)

    fun saveLastReading(readingSession: ReadingSession) {
        write(KEY_LAST_READING, readingSession)
    }

    fun getCachedTranslations(): List<Translation> = read<List<Translation>>(KEY_TRANSLATIONS).orEmpty()

    fun saveCachedTranslations(items: List<Translation>) {
        write(KEY_TRANSLATIONS, items)
    }

    fun getCachedBooks(translationId: String): List<Book> {
        return read<List<Book>>(booksKey(translationId)).orEmpty()
    }

    fun saveCachedBooks(translationId: String, items: List<Book>) {
        write(booksKey(translationId), items)
    }

    fun getCachedChapter(translationId: String, bookId: String, chapter: Int): CachedChapter? {
        val perChapter = read<CachedChapter>(chapterKey(translationId, bookId, chapter))
        if (perChapter != null) return perChapter

        // Backward compatibility for old builds that kept only a single chapter.
        return read<CachedChapter>(KEY_CACHED_CHAPTER_LEGACY)?.takeIf {
            it.translationId == translationId && it.bookId == bookId && it.chapter == chapter
        }
    }

    fun saveCachedChapter(cachedChapter: CachedChapter) {
        val key = chapterKey(cachedChapter.translationId, cachedChapter.bookId, cachedChapter.chapter)
        write(key, cachedChapter)
        write(KEY_CACHED_CHAPTER_LEGACY, cachedChapter)
        trackChapterKey(key)
    }

    fun getQuizStats(): QuizStats = read<QuizStats>(KEY_QUIZ_STATS) ?: QuizStats()

    fun saveQuizResult(isCorrect: Boolean) {
        val stats = getQuizStats()
        val newStreak = if (isCorrect) stats.streak + 1 else 0
        val newStats = stats.copy(
            totalQuestions = stats.totalQuestions + 1,
            correctAnswers = if (isCorrect) stats.correctAnswers + 1 else stats.correctAnswers,
            streak = newStreak,
            bestStreak = maxOf(stats.bestStreak, newStreak)
        )
        write(KEY_QUIZ_STATS, newStats)
    }

    fun getPrayerLog(): Map<String, Map<String, Boolean>> = read<Map<String, Map<String, Boolean>>>(KEY_PRAYER_LOG).orEmpty()

    fun logPrayer(type: String) {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        val log = getPrayerLog().toMutableMap()
        val todayLog = log[today]?.toMutableMap() ?: mutableMapOf()
        todayLog[type] = true
        log[today] = todayLog
        write(KEY_PRAYER_LOG, log)
    }

    fun getHighlights(): List<Highlight> = read<List<Highlight>>(KEY_HIGHLIGHTS).orEmpty()

    fun saveHighlight(highlight: Highlight) {
        val current = getHighlights().filterNot { it.reference == highlight.reference }.toMutableList()
        if (highlight.color.isNotBlank() || highlight.note.isNotBlank()) {
            current.add(highlight)
        }
        write(KEY_HIGHLIGHTS, current)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private fun saveBookmarks(items: List<Bookmark>) {
        write(KEY_BOOKMARKS, items)
    }

    private fun trackChapterKey(key: String) {
        val keys = read<MutableList<String>>(KEY_CHAPTER_INDEX).orEmpty().toMutableList()
        keys.remove(key)
        keys.add(0, key)

        if (keys.size > MAX_CACHED_CHAPTERS) {
            keys.drop(MAX_CACHED_CHAPTERS).forEach { staleKey ->
                prefs.edit().remove(staleKey).apply()
            }
            keys.subList(MAX_CACHED_CHAPTERS, keys.size).clear()
        }

        write(KEY_CHAPTER_INDEX, keys)
    }

    private fun booksKey(translationId: String): String = "$KEY_BOOKS_PREFIX${sanitize(translationId)}"

    private fun chapterKey(translationId: String, bookId: String, chapter: Int): String {
        return "$KEY_CHAPTER_PREFIX${sanitize(translationId)}_${sanitize(bookId)}_$chapter"
    }

    private fun sanitize(value: String): String = value.replace(Regex("[^A-Za-z0-9._-]"), "_")

    private inline fun <reified T> read(key: String): T? {
        val raw = prefs.getString(key, null) ?: return null
        return runCatching { json.decodeFromString<T>(raw) }
            .onFailure { Log.w(TAG, "Failed to decode key=$key", it) }
            .getOrNull()
    }

    private inline fun <reified T> write(key: String, value: T) {
        prefs.edit().putString(key, json.encodeToString(value)).apply()
    }

    companion object {
        private const val TAG = "LocalStore"
        private const val KEY_BOOKMARKS = "bookmarks"
        private const val KEY_LAST_READING = "last_reading"
        private const val KEY_CACHED_CHAPTER_LEGACY = "cached_chapter"
        private const val KEY_TRANSLATIONS = "cached_translations"
        private const val KEY_BOOKS_PREFIX = "cached_books_"
        private const val KEY_CHAPTER_PREFIX = "cached_chapter_"
        private const val KEY_CHAPTER_INDEX = "cached_chapter_keys"
        private const val KEY_QUIZ_STATS = "quiz_stats"
        private const val KEY_PRAYER_LOG = "prayer_log"
        private const val KEY_HIGHLIGHTS = "highlights"
        private const val MAX_CACHED_CHAPTERS = 36
    }
}
