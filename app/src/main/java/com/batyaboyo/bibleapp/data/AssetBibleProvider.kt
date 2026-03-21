package com.batyaboyo.bibleapp.data

import android.content.Context
import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

@Serializable
private data class LocalBook(
    val abbrev: String,
    val name: String,
    val chapters: List<List<String>>
)

class AssetBibleProvider(private val context: Context) : BibleApi {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedBible: List<LocalBook>? = null

    private suspend fun getBible(): List<LocalBook> = withContext(Dispatchers.IO) {
        cachedBible ?: try {
            val inputStream = context.assets.open("bible_kjv.json")
            val reader = InputStreamReader(inputStream)
            val bible: List<LocalBook> = json.decodeFromString(reader.readText())
            cachedBible = bible
            bible
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun fetchTranslations(): List<Translation> {
        return listOf(Translation("local_kjv", "King James Version (Offline)", "KJV"))
    }

    override suspend fun fetchBooks(translationId: String): List<Book> {
        return getBible().map { b ->
            Book(id = b.abbrev, name = b.name, chapters = b.chapters.size)
        }
    }

    override suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse> {
        val book = getBible().find { it.abbrev.equals(bookId, ignoreCase = true) } ?: return emptyList()
        val chapterIndex = chapter - 1
        if (chapterIndex < 0 || chapterIndex >= book.chapters.size) return emptyList()
        
        val verses = book.chapters[chapterIndex]
        return verses.mapIndexed { index, text ->
            val number = index + 1
            Verse(number = number, text = text, reference = "${book.name} $chapter:$number")
        }
    }

    override suspend fun fetchCommentaries(): List<com.batyaboyo.bibleapp.model.Commentary> {
        return emptyList()
    }

    override suspend fun fetchCommentaryChapter(commentaryId: String, bookId: String, chapter: Int): com.batyaboyo.bibleapp.model.CommentaryChapter? {
        return null
    }

    override suspend fun fetchVerse(translationId: String, bookId: String, chapter: Int, verse: Int): Verse? {
        val chapterVerses = fetchChapter(translationId, bookId, chapter)
        return chapterVerses.find { it.number == verse }
    }
}
