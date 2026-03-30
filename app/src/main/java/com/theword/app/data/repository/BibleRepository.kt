package com.theword.app.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theword.app.data.api.BibleApiService
import com.theword.app.data.api.ContentItemDto
import com.theword.app.data.local.*
import com.theword.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.theword.app.data.api.CompleteTranslationResponse
import android.content.Context
import com.theword.app.data.embedded.BundledBibleProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class BibleRepository(
    private val api: BibleApiService,
    private val db: AppDatabase,
    val prefs: PreferencesManager,
    private val bundledProvider: BundledBibleProvider
) {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    
    // Type adapter for ChapterContent list
    private val chapterListType = Types.newParameterizedType(
        List::class.java, 
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    )
    private val chapterAdapter = moshi.adapter<List<Map<String, Any>>>(chapterListType)
    
    // In-memory cache for session
    private val translationsCache = ConcurrentHashMap<String, List<Translation>>()
    private val booksCache = ConcurrentHashMap<String, List<BibleBook>>()
    private val chapterCache: MutableMap<String, List<ChapterContent>> = Collections.synchronizedMap(
        object : LinkedHashMap<String, List<ChapterContent>>(50, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, List<ChapterContent>>): Boolean {
                return size > 50
            }
        }
    )
    private val commentariesCache = ConcurrentHashMap<String, List<Commentary>>()

    // ---- Translations ----

    suspend fun getTranslations(): List<Translation> {
        translationsCache["all"]?.let { return it }

        val sortComparator = compareBy<Translation> {
            val priority = listOf("BSB", "eng_kjv", "eng_kja", "ENGWEBP", "eng_web", "eng_bbe", "eng_wbs", "eng_webc")
            val idx = priority.indexOf(it.id)
            if (idx >= 0) idx else 100
        }

        // Start with bundled translations (always available, persistent)
        val merged = mutableMapOf<String, Translation>()
        bundledProvider.getBundledTranslations().forEach { merged[it.id] = it }

        // Merge with DB translations (user-downloaded & API-discovered)
        val cached = db.bibleCacheDao().getTranslations()
        cached.forEach { entity ->
            merged[entity.id] = Translation(
                entity.id, entity.name, entity.shortName, entity.language,
                entity.isDownloaded || bundledProvider.isBundled(entity.id)
            )
        }

        if (cached.isNotEmpty()) {
            val list = merged.values.sortedWith(sortComparator)
            translationsCache["all"] = list
            return list
        }

        // DB empty — try network to discover additional translations
        try {
            val response = api.getTranslations()
            val all = response.translations ?: emptyList()
            val english = all
                .filter { it.language == "eng" || it.languageEnglishName == "English" }
                .map { Translation(it.id, it.name ?: it.englishName ?: it.id, it.shortName ?: it.id, it.language ?: "eng") }

            db.bibleCacheDao().insertTranslations(english.map {
                TranslationCacheEntity(it.id, it.name, it.shortName, it.language)
            })

            // Restore isDownloaded for translations that have cached chapter data
            for (t in english) {
                val chapterCount = db.bibleCacheDao().getChapterCount(t.id)
                if (chapterCount > 0) {
                    db.bibleCacheDao().markTranslationDownloaded(t.id, true)
                }
            }

            val updatedFromDb = db.bibleCacheDao().getTranslations()
            val result = updatedFromDb.map {
                Translation(it.id, it.name, it.shortName, it.language, it.isDownloaded)
            }.sortedWith(sortComparator)
            translationsCache["all"] = result
            return result
        } catch (e: Exception) {
            // Network failed — return bundled translations (always available)
            val list = merged.values.sortedWith(sortComparator)
            translationsCache["all"] = list
            return list
        }
    }

    // ---- Commentaries ----

    suspend fun getCommentaries(): List<Commentary> {
        commentariesCache["all"]?.let { return it }
        return try {
            val response = api.getCommentaries()
            val list = (response.commentaries ?: emptyList())
                .map { Commentary(it.id, it.name ?: it.englishName ?: it.id) }
            commentariesCache["all"] = list
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ---- Books ----

    suspend fun getBooks(translationId: String): List<BibleBook> {
        booksCache[translationId]?.let { return it }
        
        // Try Room cache first
        val cached = db.bibleCacheDao().getBooks(translationId)
        if (cached.isNotEmpty()) {
            val list = cached.map { BibleBook(it.bookId, it.name, it.totalChapters, it.order) }
            booksCache[translationId] = list
            return list
        }

        // Try persistent bundled Bible data from assets
        val bundledBooks = bundledProvider.getBooks(translationId)
        if (bundledBooks != null) {
            booksCache[translationId] = bundledBooks
            return bundledBooks
        }

        // Try network
        try {
            val response = api.getBooks(translationId)
            val books = (response.books ?: emptyList()).map {
                BibleBook(it.id, it.name ?: it.commonName ?: it.id, it.numberOfChapters, it.order)
            }
            
            // Save to local cache
            db.bibleCacheDao().insertBooks(books.map {
                BookCacheEntity("${translationId}_${it.id}", translationId, it.id, it.name, it.chapters, it.order)
            })
            
            booksCache[translationId] = books
            return books
        } catch (e: Exception) {
            return getFallbackBooks()
        }
    }

    // ---- Chapter ----

    suspend fun getChapter(translationId: String, bookId: String, chapter: Int): List<ChapterContent> {
        val key = "$translationId|$bookId|$chapter"
        chapterCache[key]?.let { return it }
        
        // Try Room cache first
        val cached = db.bibleCacheDao().getChapter(translationId, bookId, chapter)
        if (cached != null) {
            try {
                val list = deserializeChapterContent(cached.contentJson)
                chapterCache[key] = list
                return list
            } catch (jsonEx: Exception) {
                // Ignore and proceed
            }
        }

        // Try persistent bundled Bible data from assets
        val bundledContent = bundledProvider.getChapter(translationId, bookId, chapter)
        if (bundledContent != null) {
            chapterCache[key] = bundledContent
            return bundledContent
        }

        // Try network
        try {
            val response = api.getChapter(translationId, bookId, chapter)
            val footnotes = (response.chapter?.footnotes ?: emptyList()).associateBy { it.noteId }
            val content = (response.chapter?.content ?: emptyList()).mapNotNull { item ->
                when (item.type) {
                    "heading" -> {
                        val text = extractTextFromContent(item.content)
                        ChapterContent.Heading(text)
                    }
                    "verse" -> {
                        val number = item.number ?: return@mapNotNull null
                        val parts = parseVerseParts(item.content, footnotes)
                        val plainText = parts.joinToString("") {
                            when (it) {
                                is VersePart.Text -> it.text
                                is VersePart.Poem -> it.text
                                is VersePart.Footnote -> ""
                            }
                        }.trim()
                        ChapterContent.VerseContent(Verse(number, plainText, parts))
                    }
                    "line_break" -> ChapterContent.LineBreak()
                    "hebrew_subtitle" -> {
                        val text = extractTextFromContent(item.content)
                        ChapterContent.HebrewSubtitle(text)
                    }
                    else -> null
                }
            }
            
            // Save to local cache as JSON
            db.bibleCacheDao().insertChapter(
                ChapterCacheEntity(
                    id = "${translationId}_${bookId}_$chapter",
                    translationId = translationId,
                    bookId = bookId,
                    chapter = chapter,
                    contentJson = chapterAdapter.toJson(content.map { it.toMap() })
                )
            )
            
            chapterCache[key] = content
            return content
        } catch (e: Exception) {
            return emptyList()
        }
    }

    // Helper to serialize ChapterContent manually for Moshi
    private fun ChapterContent.toMap(): Map<String, Any> {
        return when (this) {
            is ChapterContent.Heading -> mapOf("type" to "Heading", "text" to text)
            is ChapterContent.VerseContent -> mapOf(
                "type" to "VerseContent",
                "verse" to mapOf(
                    "number" to verse.number,
                    "text" to verse.text
                )
            )
            is ChapterContent.LineBreak -> mapOf("type" to "LineBreak")
            is ChapterContent.HebrewSubtitle -> mapOf("type" to "HebrewSubtitle", "text" to text)
        }
    }

    private fun deserializeChapterContent(json: String): List<ChapterContent> {
        val rawList = chapterAdapter.fromJson(json) ?: return emptyList()
        
        return rawList.mapNotNull { map ->
            when (map["type"]) {
                "Heading" -> ChapterContent.Heading(map["text"] as String)
                "VerseContent" -> {
                    val verseMap = map["verse"] as Map<String, Any>
                    val number = (verseMap["number"] as Double).toInt() // Moshi parses numbers as Double by default
                    val text = verseMap["text"] as String
                    ChapterContent.VerseContent(Verse(number, text, listOf(VersePart.Text(text))))
                }
                "LineBreak" -> ChapterContent.LineBreak()
                "HebrewSubtitle" -> ChapterContent.HebrewSubtitle(map["text"] as String)
                else -> null
            }
        }
    }

    suspend fun getVerseText(translationId: String, bookId: String, chapter: Int, verseNumber: Int): String? {
        val content = getChapter(translationId, bookId, chapter)
        return content.filterIsInstance<ChapterContent.VerseContent>()
            .find { it.verse.number == verseNumber }?.verse?.text
    }

    // ---- Commentary ----

    suspend fun getCommentaryChapter(commentaryId: String, bookId: String, chapter: Int): CommentaryContent? {
        return try {
            val response = api.getCommentaryChapter(commentaryId, bookId, chapter)
            val ch = response.chapter ?: return null
            val name = response.commentary?.name ?: "Commentary"
            val bookName = response.book?.name ?: ""
            val sections = (ch.content ?: emptyList()).mapNotNull { item ->
                if (item.type == "verse") {
                    val text = extractTextFromContent(item.content)
                    CommentarySection(item.number?.toString() ?: "", text)
                } else null
            }
            CommentaryContent(name, bookName, ch.number, ch.introduction, sections)
        } catch (e: Exception) {
            null
        }
    }

    // ---- Bookmarks ----

    fun getAllBookmarks(): Flow<List<Bookmark>> =
        db.bookmarkDao().getAllBookmarks().map { list ->
            list.map { Bookmark(it.reference, it.text, it.collection, it.bookmarkedAt) }
        }

    suspend fun isBookmarked(reference: String): Boolean =
        db.bookmarkDao().getBookmark(reference) != null

    suspend fun toggleBookmark(reference: String, text: String) {
        val existing = db.bookmarkDao().getBookmark(reference)
        if (existing != null) {
            db.bookmarkDao().deleteByReference(reference)
        } else {
            db.bookmarkDao().insertBookmark(BookmarkEntity(reference, text))
        }
    }

    suspend fun toggleFavorite(reference: String, text: String) {
        val existing = db.bookmarkDao().getBookmark(reference)
        if (existing != null && existing.collection == "Favorites") {
            db.bookmarkDao().deleteByReference(reference)
        } else {
            db.bookmarkDao().insertBookmark(BookmarkEntity(reference, text, collection = "Favorites"))
        }
    }

    suspend fun removeBookmark(reference: String) {
        db.bookmarkDao().deleteByReference(reference)
    }

    suspend fun moveBookmarkToCollection(reference: String, collection: String?) {
        db.bookmarkDao().updateCollection(reference, collection)
    }

    // ---- Highlights ----

    fun getAllHighlights(): Flow<List<Highlight>> =
        db.highlightDao().getAllHighlights().map { list ->
            list.map { Highlight(it.reference, it.color, it.note) }
        }

    suspend fun getHighlight(reference: String): Highlight? =
        db.highlightDao().getHighlight(reference)?.let {
            Highlight(it.reference, it.color, it.note)
        }

    suspend fun saveHighlight(reference: String, color: String, note: String?) {
        if (color == "none" && note.isNullOrBlank()) {
            db.highlightDao().deleteHighlight(reference)
        } else {
            db.highlightDao().insertHighlight(HighlightEntity(reference, color, note))
        }
    }

    // ---- Reading Progress ----

    fun getAllProgress(): Flow<List<ReadingProgressEntity>> =
        db.readingProgressDao().getAllProgress()

    fun getTotalChaptersRead(): Flow<Int> =
        db.readingProgressDao().getTotalChaptersRead()

    suspend fun markChapterRead(bookId: String, chapter: Int) {
        val id = "${bookId}_$chapter"
        db.readingProgressDao().markChapterRead(
            ReadingProgressEntity(id, bookId, chapter)
        )
    }

    // ---- Quiz ----

    suspend fun getQuizResult(dateKey: String): QuizResultEntity? =
        db.quizResultDao().getResult(dateKey)

    suspend fun saveQuizResult(dateKey: String, questionsJson: String, answersJson: String, score: Int, total: Int) {
        db.quizResultDao().insertResult(
            QuizResultEntity(dateKey, questionsJson, answersJson, score, total)
        )
    }

    suspend fun getAllQuizResults(): List<QuizResultEntity> =
        db.quizResultDao().getAllResults()

    // ---- Journal ----

    fun getAllJournalEntries(): Flow<List<JournalEntry>> =
        db.journalDao().getAllEntries().map { list ->
            list.map { JournalEntry(it.id, it.title, it.content, it.timestamp) }
        }

    suspend fun saveJournalEntry(title: String, content: String, id: Long = 0) {
        db.journalDao().insertEntry(JournalEntryEntity(id, title, content))
    }

    suspend fun deleteJournalEntry(id: Long) {
        db.journalDao().deleteEntry(id)
    }

    // ---- Offline Sync ----

    suspend fun syncTranslation(translationId: String) {
        withContext(Dispatchers.IO) {
            try {
                val complete = api.getCompleteTranslation(translationId)
                saveCompleteTranslation(complete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun initializeBundledBibles(context: Context) = withContext(Dispatchers.IO) {
        try {
            val assetManager = context.assets
            val bibleFiles = assetManager.list("bibles") ?: emptyArray()

            for (fileName in bibleFiles) {
                if (!fileName.endsWith(".json")) continue

                try {
                    val jsonString = assetManager.open("bibles/$fileName").bufferedReader().use { it.readText() }
                    val response = moshi.adapter(CompleteTranslationResponse::class.java).fromJson(jsonString)

                    val t = response?.translation ?: continue
                    val translationId = t.id

                    // Check if chapters are already loaded (not just if the ID exists)
                    val existingCount = db.bibleCacheDao().getChapterCount(translationId)
                    if (existingCount > 0) continue // Already loaded, skip

                    // Insert translation metadata — marked as downloaded
                    db.bibleCacheDao().insertTranslations(listOf(
                        TranslationCacheEntity(
                            id = translationId,
                            name = t.name ?: t.englishName ?: translationId,
                            shortName = t.shortName ?: translationId,
                            language = t.language ?: "eng",
                            isDownloaded = true
                        )
                    ))

                    // Insert books
                    val bookEntities = (response.books ?: emptyList()).map { b ->
                        BookCacheEntity(
                            id = "${translationId}_${b.id}",
                            translationId = translationId,
                            bookId = b.id,
                            name = b.name ?: b.commonName ?: b.id,
                            totalChapters = b.numberOfChapters,
                            order = b.order
                        )
                    }
                    db.bibleCacheDao().insertBooks(bookEntities)

                    // Insert chapters in batches to avoid memory pressure
                    val chapterEntities = mutableListOf<ChapterCacheEntity>()
                    (response.books ?: emptyList()).forEach { book ->
                        (book.chapters ?: emptyList()).forEach { chapterDto ->
                            val ch = chapterDto.chapter ?: return@forEach
                            val chNum = ch.number
                            val footnotes = (ch.footnotes ?: emptyList()).associateBy { it.noteId }
                            val content = (ch.content ?: emptyList()).mapNotNull { item ->
                                mapContentItemToDomain(item, footnotes)
                            }
                            chapterEntities.add(
                                ChapterCacheEntity(
                                    id = "${translationId}_${book.id}_$chNum",
                                    translationId = translationId,
                                    bookId = book.id,
                                    chapter = chNum,
                                    contentJson = chapterAdapter.toJson(content.map { it.toMap() })
                                )
                            )
                            if (chapterEntities.size >= 100) {
                                db.bibleCacheDao().insertChapters(chapterEntities.toList())
                                chapterEntities.clear()
                            }
                        }
                    }
                    if (chapterEntities.isNotEmpty()) {
                        db.bibleCacheDao().insertChapters(chapterEntities)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (_: Exception) {
            // Pre-population failed; BundledBibleProvider serves as persistent fallback
        }
    }

    private suspend fun saveCompleteTranslation(complete: CompleteTranslationResponse) {
        val t = complete.translation ?: return
        val translationId = t.id
        
        // Save books
        val bookEntities = (complete.books ?: emptyList()).map { b ->
            BookCacheEntity("${translationId}_${b.id}", translationId, b.id, b.name ?: b.commonName ?: b.id, b.numberOfChapters, b.order)
        }
        db.bibleCacheDao().insertBooks(bookEntities)

        // Save chapters
        val chapterEntities = mutableListOf<ChapterCacheEntity>()
        complete.books?.forEach { book ->
            book.chapters?.forEachIndexed { index, chapterDto ->
                val chNum = chapterDto.chapter?.number ?: (index + 1)
                val footnotes = (chapterDto.chapter?.footnotes ?: emptyList()).associateBy { it.noteId }
                
                // Convert content
                val content = (chapterDto.chapter?.content ?: emptyList()).mapNotNull { item ->
                    mapContentItemToDomain(item, footnotes)
                }

                chapterEntities.add(
                    ChapterCacheEntity(
                        id = "${translationId}_${book.id}_$chNum",
                        translationId = translationId,
                        bookId = book.id,
                        chapter = chNum,
                        contentJson = chapterAdapter.toJson(content.map { it.toMap() })
                    )
                )
                
                // Batch insert every 100 chapters to avoid memory issues
                if (chapterEntities.size >= 100) {
                    db.bibleCacheDao().insertChapters(chapterEntities.toList())
                    chapterEntities.clear()
                }
            }
        }
        
        if (chapterEntities.isNotEmpty()) {
            db.bibleCacheDao().insertChapters(chapterEntities)
        }

        // Mark as downloaded
        db.bibleCacheDao().markTranslationDownloaded(translationId, true)
        
        // Clear caches
        translationsCache.remove("all")
        booksCache.remove(translationId)
    }

    // ---- Helpers ----

    private fun mapContentItemToDomain(
        item: com.theword.app.data.api.ContentItemDto,
        footnotes: Map<Int, com.theword.app.data.api.FootnoteDto>
    ): ChapterContent? {
        return when (item.type) {
            "heading" -> ChapterContent.Heading(extractTextFromContent(item.content))
            "verse" -> {
                val number = item.number ?: return null
                val parts = parseVerseParts(item.content, footnotes)
                val plainText = parts.joinToString("") {
                    when (it) {
                        is VersePart.Text -> it.text
                        is VersePart.Poem -> it.text
                        is VersePart.Footnote -> ""
                        else -> ""
                    }
                }.trim()
                ChapterContent.VerseContent(Verse(number, plainText, parts))
            }
            "line_break" -> ChapterContent.LineBreak()
            "hebrew_subtitle" -> ChapterContent.HebrewSubtitle(extractTextFromContent(item.content))
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractTextFromContent(content: Any?): String {
        return when (content) {
            is String -> content
            is List<*> -> (content as List<Any>).joinToString("") { part ->
                when (part) {
                    is String -> part
                    is Map<*, *> -> (part["text"] as? String) ?: ""
                    else -> ""
                }
            }
            is Map<*, *> -> (content["text"] as? String) ?: ""
            else -> ""
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseVerseParts(content: Any?, footnotes: Map<Int, com.theword.app.data.api.FootnoteDto>): List<VersePart> {
        if (content !is List<*>) return emptyList()
        return (content as List<Any>).mapNotNull { part ->
            when (part) {
                is String -> VersePart.Text(part)
                is Map<*, *> -> {
                    val noteId = (part["noteId"] as? Number)?.toInt()
                    val text = part["text"] as? String
                    val poem = (part["poem"] as? Number)?.toInt()
                    when {
                        noteId != null -> {
                            val fn = footnotes[noteId]
                            if (fn != null) VersePart.Footnote(fn.caller ?: "*", fn.text ?: "")
                            else null
                        }
                        text != null && poem != null -> VersePart.Poem(text, poem)
                        text != null -> VersePart.Text(text)
                        else -> null
                    }
                }
                else -> null
            }
        }
    }

    private fun getFallbackBooks(): List<BibleBook> {
        val otBooks = listOf(
            "Genesis" to 50, "Exodus" to 40, "Leviticus" to 27, "Numbers" to 36, "Deuteronomy" to 34,
            "Joshua" to 24, "Judges" to 21, "Ruth" to 4, "1 Samuel" to 31, "2 Samuel" to 24,
            "1 Kings" to 22, "2 Kings" to 25, "1 Chronicles" to 29, "2 Chronicles" to 36,
            "Ezra" to 10, "Nehemiah" to 13, "Esther" to 10, "Job" to 42, "Psalms" to 150,
            "Proverbs" to 31, "Ecclesiastes" to 12, "Song of Solomon" to 8, "Isaiah" to 66,
            "Jeremiah" to 52, "Lamentations" to 5, "Ezekiel" to 48, "Daniel" to 12,
            "Hosea" to 14, "Joel" to 3, "Amos" to 9, "Obadiah" to 1, "Jonah" to 4,
            "Micah" to 7, "Nahum" to 3, "Habakkuk" to 3, "Zephaniah" to 3, "Haggai" to 2,
            "Zechariah" to 14, "Malachi" to 4
        )
        val ntBooks = listOf(
            "Matthew" to 28, "Mark" to 16, "Luke" to 24, "John" to 21, "Acts" to 28,
            "Romans" to 16, "1 Corinthians" to 16, "2 Corinthians" to 13, "Galatians" to 6,
            "Ephesians" to 6, "Philippians" to 4, "Colossians" to 4, "1 Thessalonians" to 5,
            "2 Thessalonians" to 3, "1 Timothy" to 6, "2 Timothy" to 4, "Titus" to 3,
            "Philemon" to 1, "Hebrews" to 13, "James" to 5, "1 Peter" to 5, "2 Peter" to 3,
            "1 John" to 5, "2 John" to 1, "3 John" to 1, "Jude" to 1, "Revelation" to 22
        )
        val bookNameToId = mapOf(
            "Genesis" to "GEN", "Exodus" to "EXO", "Leviticus" to "LEV", "Numbers" to "NUM",
            "Deuteronomy" to "DEU", "Joshua" to "JOS", "Judges" to "JDG", "Ruth" to "RUT",
            "1 Samuel" to "1SA", "2 Samuel" to "2SA", "1 Kings" to "1KI", "2 Kings" to "2KI",
            "1 Chronicles" to "1CH", "2 Chronicles" to "2CH", "Ezra" to "EZR", "Nehemiah" to "NEH",
            "Esther" to "EST", "Job" to "JOB", "Psalms" to "PSA", "Proverbs" to "PRO",
            "Ecclesiastes" to "ECC", "Song of Solomon" to "SNG", "Isaiah" to "ISA", "Jeremiah" to "JER",
            "Lamentations" to "LAM", "Ezekiel" to "EZK", "Daniel" to "DAN", "Hosea" to "HOS",
            "Joel" to "JOL", "Amos" to "AMO", "Obadiah" to "OBA", "Jonah" to "JON",
            "Micah" to "MIC", "Nahum" to "NAM", "Habakkuk" to "HAB", "Zephaniah" to "ZEP",
            "Haggai" to "HAG", "Zechariah" to "ZEC", "Malachi" to "MAL",
            "Matthew" to "MAT", "Mark" to "MRK", "Luke" to "LUK", "John" to "JHN",
            "Acts" to "ACT", "Romans" to "ROM", "1 Corinthians" to "1CO", "2 Corinthians" to "2CO",
            "Galatians" to "GAL", "Ephesians" to "EPH", "Philippians" to "PHP",
            "Colossians" to "COL", "1 Thessalonians" to "1TH", "2 Thessalonians" to "2TH",
            "1 Timothy" to "1TI", "2 Timothy" to "2TI", "Titus" to "TIT", "Philemon" to "PHM",
            "Hebrews" to "HEB", "James" to "JAS", "1 Peter" to "1PE", "2 Peter" to "2PE",
            "1 John" to "1JN", "2 John" to "2JN", "3 John" to "3JN", "Jude" to "JUD",
            "Revelation" to "REV"
        )
        return otBooks.mapIndexed { i, (name, ch) ->
            BibleBook(bookNameToId[name] ?: name, name, ch, i + 1)
        } + ntBooks.mapIndexed { i, (name, ch) ->
            BibleBook(bookNameToId[name] ?: name, name, ch, 40 + i)
        }
    }
}
