package com.batyaboyo.bibleapp.data

import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse

interface BibleApi {
    suspend fun fetchTranslations(): List<Translation>

    suspend fun fetchBooks(translationId: String): List<Book>

    suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse>

    suspend fun fetchCommentaries(): List<com.batyaboyo.bibleapp.model.Commentary>
    suspend fun fetchCommentaryChapter(commentaryId: String, bookId: String, chapter: Int): com.batyaboyo.bibleapp.model.CommentaryChapter?
    suspend fun fetchVerse(translationId: String, bookId: String, chapter: Int, verse: Int): Verse?
}
