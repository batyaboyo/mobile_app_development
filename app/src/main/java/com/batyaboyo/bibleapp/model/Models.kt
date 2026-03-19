package com.batyaboyo.bibleapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    val id: String,
    val name: String,
    val shortName: String
)

@Serializable
data class Book(
    val id: String,
    val name: String,
    val chapters: Int
)

@Serializable
data class Verse(
    val number: Int,
    val text: String,
    val reference: String
)

@Serializable
data class Bookmark(
    val reference: String,
    val text: String,
    val version: String
)

@Serializable
data class Story(
    val id: String,
    val title: String,
    val testament: String,
    val reference: String,
    val snippet: String,
    val moral: String
)

@Serializable
data class QuizQuestion(
    val category: String,
    val question: String,
    val options: List<String>,
    val answerIndex: Int,
    val reference: String
)

@Serializable
data class ReadingSession(
    val translationId: String,
    val bookId: String,
    val chapter: Int
)

@Serializable
data class CachedChapter(
    val translationId: String,
    val bookId: String,
    val chapter: Int,
)

@Serializable
data class QuizStats(
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0
)

@Serializable
data class Highlight(
    val reference: String,
    val color: String,
    val note: String = ""
)

@Serializable
data class Prayer(
    val type: String,
    val title: String,
    val verse: String,
    val verseRef: String,
    val text: String,
    val closing: String
)
