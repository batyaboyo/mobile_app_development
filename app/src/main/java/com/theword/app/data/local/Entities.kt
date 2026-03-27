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
