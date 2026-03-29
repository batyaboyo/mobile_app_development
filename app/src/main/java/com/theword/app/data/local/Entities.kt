package com.theword.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val reference: String,
    val text: String,
    val collection: String? = null,
    val bookmarkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights")
data class HighlightEntity(
    @PrimaryKey val reference: String,
    val color: String,
    val note: String? = null
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey val id: String, // composite: bookId_chapter
    val bookId: String,
    val chapter: Int,
    val readAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey val dateKey: String,
    val questionsJson: String,
    val answersJson: String,
    val score: Int,
    val total: Int
)

@Entity(tableName = "bible_translations_cache")
data class TranslationCacheEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String,
    val language: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "bible_books_cache")
data class BookCacheEntity(
    @PrimaryKey val id: String, // composite: translationId_bookId
    val translationId: String,
    val bookId: String,
    val name: String,
    val totalChapters: Int,
    val order: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "bible_chapters_cache")
data class ChapterCacheEntity(
    @PrimaryKey val id: String, // composite: translationId_bookId_chapter
    val translationId: String,
    val bookId: String,
    val chapter: Int,
    val contentJson: String, // We'll store the List<ChapterContent> as JSON
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
