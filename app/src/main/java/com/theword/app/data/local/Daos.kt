package com.theword.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE collection = :collection ORDER BY bookmarkedAt DESC")
    fun getBookmarksByCollection(collection: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE reference = :reference LIMIT 1")
    suspend fun getBookmark(reference: String): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE reference = :reference")
    suspend fun deleteByReference(reference: String)

    @Query("UPDATE bookmarks SET collection = :collection WHERE reference = :reference")
    suspend fun updateCollection(reference: String, collection: String?)
}

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights WHERE reference = :reference LIMIT 1")
    suspend fun getHighlight(reference: String): HighlightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE reference = :reference")
    suspend fun deleteHighlight(reference: String)
}

@Dao
interface ReadingProgressDao {
    @Query("SELECT * FROM reading_progress")
    fun getAllProgress(): Flow<List<ReadingProgressEntity>>

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId")
    fun getProgressForBook(bookId: String): Flow<List<ReadingProgressEntity>>

    @Query("SELECT COUNT(*) FROM reading_progress")
    fun getTotalChaptersRead(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markChapterRead(progress: ReadingProgressEntity)
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getResult(dateKey: String): QuizResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity)

    @Query("SELECT COUNT(*) FROM quiz_results")
    suspend fun getTotalQuizzesTaken(): Int

    @Query("SELECT * FROM quiz_results ORDER BY dateKey DESC")
    suspend fun getAllResults(): List<QuizResultEntity>
}

@Dao
interface BibleCacheDao {
    // Translations
    @Query("SELECT * FROM bible_translations_cache")
    suspend fun getTranslations(): List<TranslationCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslations(translations: List<TranslationCacheEntity>)

    // Books
    @Query("SELECT * FROM bible_books_cache WHERE translationId = :translationId ORDER BY `order` ASC")
    suspend fun getBooks(translationId: String): List<BookCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookCacheEntity>)

    // Chapters
    @Query("SELECT * FROM bible_chapters_cache WHERE translationId = :translationId AND bookId = :bookId AND chapter = :chapter LIMIT 1")
    suspend fun getChapter(translationId: String, bookId: String, chapter: Int): ChapterCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterCacheEntity)
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<JournalEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntry(id: Long)
}
