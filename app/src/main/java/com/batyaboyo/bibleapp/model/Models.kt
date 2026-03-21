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
    val version: String,
    val collection: String? = null
)

@Serializable
data class Story(
    val id: String,
    val title: String,
    val testament: String,
    val icon: String? = null,
    val content: List<StoryPage> = emptyList(),
    val snippets: String = "",
    val moral: String? = null,
    val keyVerse: StoryKeyVerse? = null
)

@Serializable
data class StoryPage(
    val title: String? = null,
    val text: String
)

@Serializable
data class StoryKeyVerse(
    val text: String,
    val ref: String
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
    val verses: List<Verse>,
    val timestamp: Long
)

@Serializable
data class Commentary(
    val id: String,
    val name: String? = null,
    val englishName: String? = null,
    val language: String? = null,
    val languageEnglishName: String? = null
)

@Serializable
data class CommentaryChapter(
    val commentary: Commentary? = null,
    val book: Book? = null,
    val chapter: CommentaryChapterDetail? = null
)

@Serializable
data class CommentaryChapterDetail(
    val number: Int,
    val introduction: String? = null,
    val content: List<CommentaryEntry>? = null
)

@Serializable
data class CommentaryEntry(
    val type: String,
    val number: String? = null,
    val content: List<String>? = null
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
