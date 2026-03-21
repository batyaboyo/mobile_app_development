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
            val content = inputStream.bufferedReader().use { it.readText() }
            // Strip UTF-8 BOM if present
            val cleanContent = if (content.startsWith('\uFEFF')) content.substring(1) else content
            val bible: List<LocalBook> = json.decodeFromString(cleanContent)
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

    private fun mapId(id: String): String {
        return when (id.uppercase()) {
            "GEN" -> "gn"; "EXO" -> "ex"; "LEV" -> "lv"; "NUM" -> "nm"; "DEU" -> "dt";
            "JOS" -> "js"; "JDG" -> "jg"; "RUT" -> "rt"; "1SA" -> "1sm"; "2SA" -> "2sm";
            "1KI" -> "1kgs"; "2KI" -> "2kgs"; "1CH" -> "1ch"; "2CH" -> "2ch"; "EZR" -> "ezr";
            "NEH" -> "ne"; "EST" -> "et"; "JOB" -> "job"; "PSA" -> "ps"; "PRO" -> "prv";
            "ECC" -> "ec"; "SNG" -> "so"; "ISA" -> "is"; "JER" -> "jr"; "LAM" -> "lm";
            "EZK" -> "ez"; "DAN" -> "dn"; "HOS" -> "ho"; "JOL" -> "jl"; "AMO" -> "am";
            "OBA" -> "ob"; "JON" -> "jn"; "MIC" -> "mi"; "NAM" -> "na"; "HAB" -> "hk";
            "ZEP" -> "zp"; "HAG" -> "hg"; "ZEC" -> "zc"; "MAL" -> "ml"; "MAT" -> "mt";
            "MRK" -> "mk"; "LUK" -> "lk"; "JHN" -> "jo"; "ACT" -> "act"; "ROM" -> "rm";
            "1CO" -> "1co"; "2CO" -> "2co"; "GAL" -> "gl"; "EPH" -> "eph"; "PHP" -> "ph";
            "COL" -> "cl"; "1TH" -> "1ts"; "2TH" -> "2ts"; "1TI" -> "1tm"; "2TI" -> "2tm";
            "TIT" -> "tt"; "PHM" -> "phm"; "HEB" -> "hb"; "JAS" -> "jm"; "1PE" -> "1pe";
            "2PE" -> "2pe"; "1JN" -> "1jo"; "2JN" -> "2jo"; "3JN" -> "3jo"; "JUD" -> "jd";
            "REV" -> "re"
            else -> id.lowercase()
        }
    }

    override suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse> {
        val mappedId = mapId(bookId)
        val book = getBible().find { it.abbrev.equals(mappedId, ignoreCase = true) || it.abbrev.equals(bookId, ignoreCase = true) } ?: return emptyList()
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
        val mappedId = mapId(bookId)
        val chapterVerses = fetchChapter(translationId, mappedId, chapter)
        return chapterVerses.find { it.number == verse }
    }
}
