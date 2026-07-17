package com.theword.app.data.embedded

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theword.app.data.api.CompleteTranslationResponse
import com.theword.app.data.api.ContentItemDto
import com.theword.app.data.api.FootnoteDto
import com.theword.app.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Reads Bible data directly from bundled JSON assets.
 * This is the authoritative, persistent offline data source — Room is just a cache on top.
 * Even if Room DB is wiped, this provider always has the data.
 */
class BundledBibleProvider(private val context: Context) {

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val bundledFiles = mapOf(
        "BSB" to "bibles/BSB.json",
        "eng_kjv" to "bibles/eng_kjv.json",
        "ENGWEBP" to "bibles/ENGWEBP.json"
    )

    private var cachedId: String? = null
    private var cachedParsed: ParsedBible? = null
    private val mutex = Mutex()

    data class ParsedBible(
        val books: List<BibleBook>,
        val chapters: Map<String, Map<Int, List<ChapterContent>>>
    )

    fun isBundled(id: String) = id in bundledFiles

    fun getBundledTranslations() = listOf(
        Translation("BSB", "Berean Standard Bible", "BSB", "eng", true),
        Translation("eng_kjv", "King James Version", "KJV", "eng", true),
        Translation("ENGWEBP", "World English Bible", "WEB", "eng", true)
    )

    suspend fun getBooks(translationId: String): List<BibleBook>? {
        if (!isBundled(translationId)) return null
        return load(translationId)?.books
    }

    suspend fun getChapter(translationId: String, bookId: String, chapter: Int): List<ChapterContent>? {
        if (!isBundled(translationId)) return null
        return load(translationId)?.chapters?.get(bookId)?.get(chapter)
    }

    private suspend fun load(id: String): ParsedBible? = mutex.withLock {
        if (cachedId == id && cachedParsed != null) return cachedParsed
        val parsed = withContext(Dispatchers.IO) { parse(id) }
        if (parsed != null) { cachedId = id; cachedParsed = parsed }
        parsed
    }

    private fun parse(translationId: String): ParsedBible? {
        val path = bundledFiles[translationId] ?: return null
        return try {
            val json = context.assets.open(path).bufferedReader().use { it.readText() }
            val resp = moshi.adapter(CompleteTranslationResponse::class.java).fromJson(json) ?: return null

            val books = (resp.books ?: emptyList()).map { b ->
                BibleBook(b.id, b.name ?: b.commonName ?: b.id, b.numberOfChapters, b.order)
            }

            val chapters = mutableMapOf<String, MutableMap<Int, List<ChapterContent>>>()
            (resp.books ?: emptyList()).forEach { book ->
                val bookCh = mutableMapOf<Int, List<ChapterContent>>()
                (book.chapters ?: emptyList()).forEach { dto ->
                    val ch = dto.chapter ?: return@forEach
                    val fn = (ch.footnotes ?: emptyList()).associateBy { it.noteId }
                    bookCh[ch.number] = (ch.content ?: emptyList()).mapNotNull { mapItem(it, fn) }
                }
                chapters[book.id] = bookCh
            }
            ParsedBible(books, chapters)
        } catch (e: Exception) { e.printStackTrace(); null }
    }

    // ---- Parsing helpers (same logic as BibleRepository) ----

    private fun mapItem(item: ContentItemDto, footnotes: Map<Int, FootnoteDto>): ChapterContent? {
        return when (item.type) {
            "heading" -> ChapterContent.Heading(extractText(item.content))
            "verse" -> {
                val n = item.number ?: return null
                val parts = parseParts(item.content, footnotes)
                val text = parts.joinToString("") {
                    when (it) { is VersePart.Text -> it.text; is VersePart.Poem -> it.text; is VersePart.Footnote -> "" }
                }.trim()
                ChapterContent.VerseContent(Verse(n, text, parts))
            }
            "line_break" -> ChapterContent.LineBreak()
            "hebrew_subtitle" -> ChapterContent.HebrewSubtitle(extractText(item.content))
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractText(content: Any?): String = when (content) {
        is String -> content
        is List<*> -> (content as List<Any>).joinToString("") { p ->
            when (p) { is String -> p; is Map<*, *> -> (p["text"] as? String) ?: ""; else -> "" }
        }
        is Map<*, *> -> (content["text"] as? String) ?: ""
        else -> ""
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseParts(content: Any?, footnotes: Map<Int, FootnoteDto>): List<VersePart> {
        if (content !is List<*>) return emptyList()
        return (content as List<Any>).mapNotNull { part ->
            when (part) {
                is String -> VersePart.Text(part)
                is Map<*, *> -> {
                    val noteId = (part["noteId"] as? Number)?.toInt()
                    val text = part["text"] as? String
                    val poem = (part["poem"] as? Number)?.toInt()
                    when {
                        noteId != null -> footnotes[noteId]?.let { VersePart.Footnote(it.caller ?: "*", it.text ?: "") }
                        text != null && poem != null -> VersePart.Poem(text, poem)
                        text != null -> VersePart.Text(text)
                        else -> null
                    }
                }
                else -> null
            }
        }
    }
}
