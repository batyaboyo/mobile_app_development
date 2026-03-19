package com.batyaboyo.bibleapp.data

import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse

interface BibleApi {
    suspend fun fetchTranslations(): List<Translation>

    suspend fun fetchBooks(translationId: String): List<Book>

    suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse>
}
