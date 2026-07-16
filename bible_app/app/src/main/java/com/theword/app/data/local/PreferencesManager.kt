package com.theword.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theword_prefs")

class PreferencesManager(private val context: Context) {

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val BIBLE_VERSION = stringPreferencesKey("bible_version")
        val COMMENTARY = stringPreferencesKey("commentary")
        val LAST_BOOK_ID = stringPreferencesKey("last_book_id")
        val LAST_BOOK_NAME = stringPreferencesKey("last_book_name")
        val LAST_CHAPTER = intPreferencesKey("last_chapter")
        val COLLECTIONS = stringPreferencesKey("collections")
    }

    val darkMode: Flow<Boolean> = context.dataStore.data.map { it[Keys.DARK_MODE] ?: false }
    val bibleVersion: Flow<String> = context.dataStore.data.map { it[Keys.BIBLE_VERSION] ?: "BSB" }
    val commentary: Flow<String> = context.dataStore.data.map { it[Keys.COMMENTARY] ?: "" }
    val collections: Flow<String> = context.dataStore.data.map {
        it[Keys.COLLECTIONS] ?: "Favorites,Promises,Comfort"
    }

    val lastBookId: Flow<String?> = context.dataStore.data.map { it[Keys.LAST_BOOK_ID] }
    val lastBookName: Flow<String?> = context.dataStore.data.map { it[Keys.LAST_BOOK_NAME] }
    val lastChapter: Flow<Int?> = context.dataStore.data.map { it[Keys.LAST_CHAPTER] }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun setBibleVersion(version: String) {
        context.dataStore.edit { it[Keys.BIBLE_VERSION] = version }
    }

    suspend fun setCommentary(commentary: String) {
        context.dataStore.edit { it[Keys.COMMENTARY] = commentary }
    }

    suspend fun setLastPosition(bookId: String, bookName: String, chapter: Int) {
        context.dataStore.edit {
            it[Keys.LAST_BOOK_ID] = bookId
            it[Keys.LAST_BOOK_NAME] = bookName
            it[Keys.LAST_CHAPTER] = chapter
        }
    }

    suspend fun setCollections(collections: String) {
        context.dataStore.edit { it[Keys.COLLECTIONS] = collections }
    }
}
